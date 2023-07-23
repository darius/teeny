; Syntax and shorthands

(define-macro 'define (lambda (args) (cons 'set! args)))

(define = equal?)

(define not (lambda (x) (eq? x #f)))

(define caar (lambda (lst) (car (car lst))))
(define cdar (lambda (lst) (cdr (car lst))))
(define caddr (lambda (lst) (car (cddr lst))))
(define cadddr (lambda (lst) (cadr (cddr lst))))

; I/O

(define print
  (lambda (object)
    (begin
      (write object)
      (newline))))

; The least dispensable list operations

(define length
  (lambda (lst)
    ((lambda (n)
       (begin
	 (while lst
           (set! n (+ n 1))
	   (set! lst (cdr lst)))
	 n))
     0)))

(define append
  (lambda (L1 L2)
    (if (null? L1)
        L2
        (cons (car L1)
              (append (cdr L1) L2)))))

(define map
  (lambda (proc lst)
    ((lambda (result)
       (begin
	 (while (pair? lst)
           (set! result (cons (proc (car lst)) result))
	   (set! lst (cdr lst)))
	 (reverse! result)))
     '())))

(define for-each
  (lambda (proc lst)
    (while (pair? lst)
      (proc (car lst))
      (set! lst (cdr lst)))))

; Macros

(define-macro 'and
  (lambda (args)
    (if (null? args)
        #t
        (if (null? (cdr args))
            (car args)
            (list 'if (car args) (cons 'and (cdr args)))))))

(define-macro 'or
  (lambda (args)
    (if (null? args)
        #f
        (if (null? (cdr args))
            (car args)
            (list
              ((lambda (test-var)
                (list 'lambda (list test-var)
                  (list 'if test-var test-var (cons 'or (cdr args)))))
               (gensym))
              (car args))))))

(define-macro 'let
  (lambda (args)
    (cons
      (cons 'lambda (cons (map car (car args)) (cdr args)))
      (map cadr (car args)))))

(define make-begin
  (lambda (lst)
    (if (null? (cdr lst))
        (car lst)
        (cons 'begin lst))))

(define-macro 'cond
  (lambda (clauses)
    (if (null? clauses)
        #f
        (if (eq? (caar clauses) 'else)
            (make-begin (cdar clauses))
            (list 'if
                  (caar clauses)
                  (make-begin (cdar clauses))
                  (cons 'cond (cdr clauses)))))))



; List-processing procedures

(define reverse
  (lambda (L) (revappend L '())))

(define revappend
  (lambda (L1 L2)
    (begin
      (while (pair? L1)
        (set! L2 (cons (car L1) L2))
	(set! L1 (cdr L1)))
      L2)))

(define append!
  (lambda (L1 L2)
    (begin
      (set-cdr! (last-pair L1) L2)
      L1)))

(define last-pair
  (lambda (lst)
    (begin
      (while (pair? (cdr lst))
        (set! lst (cdr lst)))
      lst)))

(define memq (lambda (x lst) (member:test eq? x lst)))
(define member (lambda (x lst) (member:test equal? x lst)))

(define member:test 
  (lambda (=? x lst)
    (begin
      (while (and (pair? lst) (not (=? x (car lst))))
        (set! lst (cdr lst)))
      lst)))

(define assq (lambda (key pairs) (assoc:test eq? key pairs)))
(define assoc (lambda (key pairs) (assoc:test equal? key pairs)))

(define assoc:test
  (lambda (=? key pairs)
    (begin
      (while (and (pair? pairs) (not (=? key (caar pairs))))
        (set! pairs (cdr pairs)))
      (and pairs (car pairs)))))

(define sublis
  (lambda (a-list exp)
    (cond
      ((null? exp) '())
      ((atom? exp)
       (let ((binding (assq exp a-list)))
         (if binding (cdr binding) exp)))
      (else
        (cons (sublis a-list (car exp))
              (sublis a-list (cdr exp)))))))
    
(define remove
  (lambda (key lst)
    (if (null? lst)
        '()
        (if (eq? key (car lst))
            (cdr lst)
            (cons (car lst)
                  (remove key (cdr lst)))))))

(define list-ref
  (lambda (lst n)
    (if (null? lst) 
        '()
        (if (= n 0) 
            (car lst)
            (list-ref (cdr lst) (- n 1))))))



; Numeric procedures

(define > (lambda (n1 n2) (< n2 n1)))
(define <= (lambda (n1 n2) (not (< n2 n1))))
(define >= (lambda (n1 n2) (not (< n1 n2))))

(define abs
  (lambda (n)
    (if (< n 0) (- 0 n) n)))

(define neg (lambda (n) (- 0 n)))

(define even? (lambda (n) (= (remainder n 2) 0)))
(define odd? (lambda (n) (not (even? n))))

(define min (lambda (n1 n2) (if (< n1 n2) n1 n2)))
(define max (lambda (n1 n2) (if (< n1 n2) n2 n1)))

(define expt			; Pre: power is an integer
  (lambda (base power)
    (cond ((< power 0)
	   (/ 1 (expt base (- 0 power))))
	  ((= power 0)
	   1)
	  ((even? power)
	   ((lambda (n) (* n n))
	    (expt base (quotient power 2))))
	  (else
	   (* base (expt base (- power 1)))))))

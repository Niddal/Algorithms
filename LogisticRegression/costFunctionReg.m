function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));
r = regTerm();

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta


for i = 1:m
    J = J + (-y(i)*log(hyp(X(i, :))) - (1 - y(i))*log(1 - hyp(X(i, :))));
end
J = (1/m)*J + r;

for j = 1:length(theta)
    for jj = 1:m
        grad(j) = grad(j) + (hyp(X(jj, :)) - y(jj))*X(jj, j);
    end
    grad(j) = (1/m)*grad(j);
    if j > 1
        grad(j) = grad(j) + (lambda/m)*theta(j);
    end
end

%additional cost - penalize higher order terms
function a = regTerm()
    a = 0;
    for ii = 2:length(theta) %don't regularize the first term
        a = a + theta(ii)^2; 
    end
    a = (lambda/(2*m))*a;
end

function a = hyp(b)
    a = sigmoid(dot(theta, b));
end
% =============================================================

end

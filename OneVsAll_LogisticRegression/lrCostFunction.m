function [J, grad] = lrCostFunction(theta, X, y, lambda)
%LRCOSTFUNCTION Compute cost and gradient for logistic regression with 
%regularization
%   J = LRCOSTFUNCTION(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

m = length(y); % number of training examples
J = 0;
grad = zeros(size(theta));

thetaT = [0; theta(2:end)];
hyp = sigmoid(X*theta);
J = (1/m)*(-y'*log(hyp) - (1 - y')*log(1 - hyp));
J = J + (lambda/(2*m))*(thetaT' * thetaT);

beta = hyp - y;
grad = (1/m)*X'*beta;

%deal with regularization
grad = grad + (lambda/m)*thetaT;




% =============================================================

grad = grad(:);

end

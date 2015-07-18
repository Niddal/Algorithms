function [J, grad] = linearRegCostFunction(X, y, theta, lambda)
%LINEARREGCOSTFUNCTION Compute cost and gradient for regularized linear 
%regression with multiple variables
%   [J, grad] = LINEARREGCOSTFUNCTION(X, y, theta, lambda) computes the 
%   cost of using theta as the parameter for linear regression to fit the 
%   data points in X and y. Returns the cost in J and the gradient in grad

m = length(y); % number of training examples

J = 0;
grad = zeros(size(theta));


J = (1/(2*m))*sum((X*theta - y).^2) + (lambda/(2*m))*(theta(2:end)'*theta(2:end));

temp = (X*theta - y); %scalar
grad = (1/m) * X' * temp;
grad(2:end) = grad(2:end) + lambda / m * theta(2:end);

grad = grad(:);

end

function [J, grad] = costFunction(theta, X, y)
%COSTFUNCTION Compute cost and gradient for logistic regression
%   J = COSTFUNCTION(theta, X, y) computes the cost of using theta as the
%   parameter for logistic regression and the gradient of the cost
%   w.r.t. to the parameters.

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

for i = 1:m
    J = J + (-y(i)*log(hyp(X(i, :))) - (1 - y(i))*log(1 - hyp(X(i, :))));
end
J = (1/m)*J;

for j = 1:length(theta)
    for jj = 1:m
        grad(j) = grad(j) + (hyp(X(jj, :)) - y(jj))*X(jj, j);
    end
    grad(j) = (1/m)*grad(j);
end




function a = hyp(b)
    a = sigmoid(dot(theta, b));
end



% =============================================================

end


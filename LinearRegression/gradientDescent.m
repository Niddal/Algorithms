function [theta, J_history] = gradientDescent(X, y, theta, alpha, num_iters)
%GRADIENTDESCENT Performs gradient descent to learn theta
%   theta = GRADIENTDESENT(X, y, theta, alpha, num_iters) updates theta by 
%   taking num_iters gradient steps with learning rate alpha

% Initialize some useful values
m = length(y); % number of training examples
J_history = zeros(num_iters, 1);
newTheta = zeros(length(theta), 1);

for iter = 1:num_iters

    sum1 = 0;
    sum2 = 0;
    %calculate sum1
    for i = 1:m
        sum1 = sum1 + X(i, :)*theta - y(i);
        sum2 = sum2 + (X(i, :)*theta - y(i))*X(i, 2);
    end
    sum1 = (1/m)*sum1;
    sum2 = (1/m)*sum2;
    newTheta(1) = theta(1) - alpha*sum1;
    newTheta(2) = theta(2) - alpha*sum2;
    theta = newTheta;

    % ============================================================

    % Save the cost J in every iteration
    c = computeCost(X, y, theta);
    J_history(iter) = c;
    if (c < 0.00001)
        return
    end

end

end


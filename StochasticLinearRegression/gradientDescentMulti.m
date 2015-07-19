function [theta, J_history] = gradientDescentMulti(X, y, theta, alpha, num_iters)
%GRADIENTDESCENTMULTI Performs gradient descent to learn theta
%   theta = GRADIENTDESCENTMULTI(x, y, theta, alpha, num_iters) updates theta by
%   taking num_iters gradient steps with learning rate alpha

% Initialize some useful values
m = length(y); % number of training examples
J_history = zeros(num_iters, 1);
newTheta = zeros(length(theta), 1);

for iter = 1:num_iters

    % ====================== YOUR CODE HERE ======================
    % Instructions: Perform a single gradient step on the parameter vector
    %               theta. 
    %
    % Hint: While debugging, it can be useful to print out the values
    %       of the cost function (computeCostMulti) and gradient here.
    %
    sumt = zeros(1, size(X, 2)); %for each theta other than theta_0
    %calculate sum1
    for i = 1:m
        sumt(1, 1) = sumt(1, 1) + X(i, :)*theta - y(i); %deal with first column, all ones
        for feature = 2:size(X, 2) %deal with arbitrarily many additional features that are not ones
            sumt(1, feature) = sumt(1, feature) + (X(i, :)*theta - y(i))*X(i, feature);
        end

    end
    sumt = (1/m)*sumt;
    newTheta = theta - alpha*sumt';
    theta = newTheta;

    % ============================================================

    % Save the cost J in every iteration
    c = computeCost(X, y, theta);
    J_history(iter) = c;
    if (iter > 2)
        assert (J_history(iter - 1) >= c)
    end
    if (c < 0.00001)
        return
    end

end

end

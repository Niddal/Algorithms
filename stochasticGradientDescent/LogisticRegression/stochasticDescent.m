function [theta, J] = stochasticDescent(X, y, theta, alpha, lambda, num_iters)
 

m = size(X, 1);
n = size(X, 2);
window = 1000;
J = [];
Jtemp = zeros(window, 1);
theta = zeros(n, 1);
tempAlpha = alpha;


%shuffle data
count = 0;
data = [y, X];
data = data(randperm(size(data,1)),:);
y = data(:,1);
X = data(:,2:end);

for r = 1:num_iters %repeat
    for i = 1:m %for every example in X
        %gradient = (X(i, :)*theta - y(i))*X(i, :); %specific for lin reg
        [~, gradient] = costFunctionReg(theta, X(i, :), y(i), lambda);
        theta = theta - (tempAlpha*gradient);
        [Jtemp(mod(i, window)), ~] = costFunctionReg(theta, X, y, lambda); %entire data set
        if (mod(count, window) == 0) %report average cost every 1000 calcs
            %display(count);
            J = [J mean(Jtemp(Jtemp~=0))]; %dont include zeros in average!
            Jtemp = zeros(window, 1);
        end
        tempAlpha = (alpha)/r; %gradually reduce alpha
        count = count+1;
    end
end


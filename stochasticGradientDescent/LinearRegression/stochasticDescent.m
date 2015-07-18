function [ theta, J ] = stochasticDescent(X, y, theta, alpha, num_iters)
%Alpha should start out around 0.01, shouldn't be higher than 0.3, and
%monotonically decrease (with 1/x?) with num_iters. Num_iters can be anywhere from 10 to
%>1500 depending on how big the data set X is. If X is say >100, then
%num_iters can be somewhat small. Just as long as (num_iters*m) is in the
%thousands, then you're good. You should see J decrease to below 0.1 pretty
%readily. If you truly have a massive dataset, then don't "computeCost" on
%every step, take a snapshot every 10,000 steps or so. 

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
        gradient = (X(i, :)*theta - y(i))*X(i, :); %specific for lin reg
        theta = theta - (tempAlpha*gradient)';
        Jtemp(mod(i, window)) = computeCost(X, y, theta);
        if (mod(count, window) == 0) %report average cost every 1000 calcs
            J = [J mean(Jtemp(Jtemp~=0))]; %dont include zeros in average!
            Jtemp = zeros(window, 1);
        end
        tempAlpha = (alpha)/r; %gradually reduce alpha
    end
end



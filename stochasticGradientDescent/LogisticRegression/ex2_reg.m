%you will notice the train accuracy to be 83.898305 as opposed to
%83.050847 for the other version using fminunc. However, alpha  must
%initially be much higher than expected (around 2, but it decreases
%linearly with the iterations), and lambda must be much lower: around 0.005
%which is much smaller than the other version which uses 1. 
%

%% Initialization
clear ; close all; clc

%% Load Data
%  The first two columns contains the X values and the third column
%  contains the label (y).

data = load('ex2data2.txt');
X = data(:, [1, 2]); y = data(:, 3);

plotData(X, y);

% Put some labels 
hold on;

% Labels and Legend
xlabel('Microchip Test 1')
ylabel('Microchip Test 2')

% Specified in plot order
legend('y = 1', 'y = 0')
hold off;


%% =========== Part 1: Regularized Logistic Regression ============

% Add Polynomial Features

% Note that mapFeature also adds a column of ones for us, so the intercept
% term is handled
X = mapFeature(X(:,1), X(:,2));

% Initialize fitting parameters
initial_theta = zeros(size(X, 2), 1);

% Set regularization parameter lambda to 1
lambda = 1;

% Compute and display initial cost and gradient for regularized logistic
% regression
[cost, grad] = costFunctionReg(initial_theta, X, y, lambda);

fprintf('Cost at initial theta (zeros): %f\n', cost);

fprintf('\nProgram paused. Press enter to continue.\n');
pause;

%% ============= Part 2: Regularization and Accuracies =============

% Initialize fitting parameters
initial_theta = zeros(size(X, 2), 1);

% Set regularization parameter lambda to 1 (you should vary this)

lambda = 0.005; %strangely enough, in order for stochastic descent to work 
%well, you must use a much lower lambda (around .0005 to 0.005) whereas normal
%gradient descent used a lambda of around 1
alpha = 2;
num_iters = 100;

% Optimize
[theta, J] = stochasticDescent(X, y, initial_theta, alpha, lambda, num_iters);
            %fminunc(@(t)(costFunctionReg(t, X, y, lambda)), initial_theta, options);

% Plot Boundary
plotDecisionBoundary(theta, X, y);
hold on;
title(sprintf('lambda = %g', lambda))
% Labels and Legend
xlabel('Microchip Test 1')
ylabel('Microchip Test 2')
legend('y = 1', 'y = 0', 'Decision boundary')
hold off;

%plot learning curve
figure
plot(J);
hold on
legend('Log(J_{train}) averaged every 1000 grad calculations')
xlabel('Number of Steps');
ylabel('Log of the Training Cost (L2 norm of difference)');
hold off

% Compute accuracy on our training set

p = predict(theta, X);

fprintf('Train Accuracy: %f\n', mean(double(p == y)) * 100);



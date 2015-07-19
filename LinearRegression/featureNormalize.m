function [X_norm, mu, sigma] = featureNormalize(X)
%FEATURENORMALIZE Normalizes the features in X 
%   FEATURENORMALIZE(X) returns a normalized version of X where
%   the mean value of each feature is 0 and the standard deviation
%   is 1. This is often a good preprocessing step to do when
%   working with learning algorithms.

% You need to set these values correctly
X_norm = X;
mu = zeros(1, size(X, 2));
sigma = zeros(1, size(X, 2));

for feature = 1:size(X, 2)
   m = mean(X(:, feature));
   s = std(X(:, feature));
   X_norm(:, feature) = (X(:, feature) - m)/s;
   mu(1, feature) = m;
   sigma(1, feature) = s;
    
end









% ============================================================

end

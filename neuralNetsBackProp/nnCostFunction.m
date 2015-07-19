function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));


             
m = size(X, 1);
         
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));
numLayers = 3;

% Theta1 has size 25 x 401
% Theta2 has size 10 x 26
%theta is always (num units in next layer) x (num units in this layer)
% y has size 5000 x 1

thetas = {Theta1, Theta2};
littleDeltas = cell(1, numLayers-1); %as many deltas as there are thetas
bigDeltas = cell(1, numLayers-1);
bigDeltas{1} = zeros(size(Theta1));
bigDeltas{2} = zeros(size(Theta2));

activations = cell(1, numLayers); %store the activations for each layer
zees = cell(1, numLayers);

%for every example run for backprop
cost = 0;

Y = eye(num_labels);
Y = Y(y,:); % [5000, 10], where there's a 1 in the Y(i, j) spot if example
%i if its label is j, where j is 0 - 9, 0 corresponding to 10. 
 
for example = 1:m 
    ex = X(example, :)';
    currActiv = ex;    
    activations{1} = [1; currActiv]; 
    %FORWARD propogate
    for layer = 2:numLayers
        %get the matrix that describes the weights between layer l and l+1
        theta = cell2mat(thetas(layer - 1));
        
        %compute product of previous activations and weights to get next
        %layers z
        z = theta*activations{layer - 1};
        %take sigmoid to get next layers activation
        if (layer < numLayers)
            activations{layer} = [1; sigmoid(z)]; %store activation at that layer
        else
            activations{layer} = sigmoid(z);
        end
    end
    hypothesis = activations{numLayers};
    %after propogation of one example, update cost, assuming y is 10 x 5000
    cost = cost + sum(-Y(example, :)*log(hypothesis) - (1 - Y(example, :))*log(1 - hypothesis));
    
    %compute output error, assign it to the "right" delta vector (as
    %opposed to left)
    d = hypothesis - Y(example, :)';
    littleDeltas{numLayers} = num2cell(d);
    d_next = [];
    
    %BACKWARD propogate and update layers
    for layer = (numLayers - 1):-1:1
        theta = cell2mat(thetas(layer));
        thetaT = theta(:, 2:end);        
       
        d = cell2mat(littleDeltas{layer + 1}); %move to inside if below? no?
        if (layer > 1) %actually back propogating the error to calculate
            %the little delta for the layer before us
            a = activations{layer-1};
            d_next = (thetaT' * d).*sigmoidGradient(cell2mat(thetas(layer - 1))*a);%sigmoidGradient(cell2mat(A{:})); %by next we mean at layer (l-1), d2 = (Theta2Filtered' * d3) .* sigmoidGradient(z2);
            littleDeltas{layer} = num2cell(d_next);   
        end
        C = d*(activations{layer}');        
        bigDeltas{layer} = bigDeltas{layer} + C;
    end
end

%finalize cost
J = (1/m)*cost;

%specific to this NN, but in general, multipy final gradient matrix by 1/m
Theta1_grad = (1/m)*bigDeltas{1};
Theta2_grad = (1/m)*bigDeltas{2};


%regularize, specific to this NN, but in general it must be done...
Theta1Filtered = Theta1(:,2:end);
Theta2Filtered = Theta2(:,2:end);
reg = (lambda / (2*m)) * (sumsqr(Theta1Filtered(:)) + sumsqr(Theta2Filtered(:)));
J = J + reg;


Theta1_grad(:,2:end) = Theta1_grad(:,2:end) + ((lambda / m) * Theta1Filtered);
Theta2_grad(:,2:end) = Theta2_grad(:,2:end) + ((lambda / m) * Theta2Filtered);


% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
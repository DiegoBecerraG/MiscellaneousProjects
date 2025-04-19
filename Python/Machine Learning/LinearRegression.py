import numpy as np
class LinearRegression:
    def __init__(self, lr=0.001, n_iter=10, gradientdescent=0):
        self.lr = lr
        self.n_iter = n_iter
        self.weights = None
        self.bias = None
        self.convergence_threshold = 1e-5
        self.prev_cost = float('inf')
        self.cost = 0
        self.counter = 0
        self.gradientdescent = gradientdescent

    def fit(self, X, y): # this is to get the best line / coeff. and bias
        n_samples, n_features = X.shape
        self.weights = np.zeros(n_features)
        self.bias = 0
        #print(f'Initial weights: {self.weights} Initial bias: {self.bias}')
        for i in range(self.n_iter):
            y_pred = np.dot(X, self.weights) + self.bias #f(w,b) eq. #2
            #cost = (y_pred-y)**2
            self.cost = 1/(2*n_samples) * np.sum((y_pred-y)**2)
            # Check for convergence
            if abs(self.prev_cost - self.cost) < self.convergence_threshold:
                print(f"Converged after {iteration} iterations")
                break
                
            dw = (1/n_samples) * np.dot(X.T, (y_pred-y)) #eq. 4
            db = (1/n_samples) * np.sum(y_pred-y)
            
            self.weights = self.weights - self.lr * dw
            self.bias = self.bias - self.lr - db
            self.counter = i
            if (self.gradientdescent == 1):
                print(f'Gradient Descent: cost {self.cost:.4f} num_iter {self.counter} weights {self.weights} bias {self.bias}')
            
        print(f'cost {self.cost:.4f} num_iter {self.counter} weights {self.weights} bias {self.bias}')
    def predict(self, X): # predict once we have the best coeff/bias
        y_pred = np.dot(X, self.weights) + self.bias
        print(f'Gradient Descent: cost {self.cost:.4f} num_iter {self.counter} weights {self.weights} bias {self.bias}')
        return y_pred  
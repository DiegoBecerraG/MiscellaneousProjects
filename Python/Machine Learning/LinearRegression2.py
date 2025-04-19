import numpy as np
class LinearRegression2:
    def __init__(self, lr=0.001, n_iter=10):
        self.lr = lr

    def feature_normalize(self, X, mean=np.zeros(1), std=np.zeros(1)):
        
        X = np.array(X)
        if len(mean.shape) == 1 or len(std.shape) == 1:
            print(f'{X.shape}')
            mean = np.mean(X, axis=0)
            std = np.std(X, axis=0, ddof=1)
            

        X_norm = (X - mean)/std
        return X_norm, mean, std

    def compute_cost(self, X, y, theta):
        m = y.shape[0]
        h = X.dot(theta)
        J = (1/(2*m)) * ((h-y).T.dot(h-y))
        return J

    def gradient_descent(self, X, y, theta, alpha, num_iters):
        m = y.shape[0]
        J_history = np.zeros(shape=(num_iters, 1))

        for i in range(0, num_iters):
            h = X.dot(theta)
            diff_hy = h - y

            delta = (1/m) * (diff_hy.T.dot(X))
            theta = theta - (alpha * delta.T)
            J_history[i] = self.compute_cost(X, y, theta)

        return theta, J_history
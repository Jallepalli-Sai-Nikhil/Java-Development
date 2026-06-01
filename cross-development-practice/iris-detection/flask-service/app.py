from flask import Flask, request, jsonify
import numpy as np
import joblib

app = Flask(__name__)

model = joblib.load('model.pkl')

@app.route('/predict', methods=['POST'])
def predict():
    data = request.json
    
    features = np.array([[
        float(data['sepal_length']),
        float(data['sepal_width']),
        float(data['petal_length']),
        float(data['petal_width'])
    ]])
    
    prediction = int(model.predict(features)[0])
    
    return jsonify({'prediction': prediction})

if __name__ == '__main__':
    app.run(port=5000)
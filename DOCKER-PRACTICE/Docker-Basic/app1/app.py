from flask import Flask
import os

app = Flask(__name__)

APP_MESSAGE = os.getenv("APP_MESSAGE", "Default message from APP1")


@app.route("/")
def home():
    return f"Hello from APP 1 🚀 - {APP_MESSAGE}"

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
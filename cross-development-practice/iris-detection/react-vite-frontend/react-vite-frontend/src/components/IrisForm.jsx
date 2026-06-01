import { useState } from "react";
import axios from "axios";

function IrisForm() {
  const [form, setForm] = useState({
    sepal_length: "",
    sepal_width: "",
    petal_length: "",
    petal_width: "",
  });

  const [result, setResult] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async () => {
    try {
      const res = await axios.post("http://localhost:8080/iris/predict", form);

      setResult(res.data.prediction);
    } catch (err) {
      console.error(err);
      alert("Backend error");
    }
  };

  return (
    <div>
      {Object.keys(form).map((key) => (
        <div key={key}>
          <input
            name={key}
            placeholder={key}
            value={form[key]}
            onChange={handleChange}
          />
        </div>
      ))}

      <button onClick={handleSubmit}>Predict</button>

      <h2>Result: {result}</h2>
    </div>
  );
}

export default IrisForm;

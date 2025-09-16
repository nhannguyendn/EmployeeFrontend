import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import employeeService from "../services/EmployeeService";
import LoginGoogleButton from "./LoginGoogleComponent";

function LoginEmployeeFunction() {
  const [emailId, setEmailId] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = sessionStorage.getItem("accessToken");
    if (token && token.trim() !== "") {
      requestAnimationFrame(() => navigate("/employees"));
    }
  }, [navigate]);

  const validate = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailId || !emailRegex.test(emailId)) return "Invalid email address";
    if (!password.trim()) return "Password cannot be empty";
    return "";
  };

  const login = async (e) => {
    e.preventDefault();
    const error = validate();
    if (error) return setMessage(error);

    setLoading(true);
    setMessage("Logging in...");
    try {
      const res = await employeeService.login(emailId, password);
      if (res == null || String(res).trim() === "") {
        setMessage("Email or Password is wrong");
      } else {
        navigate("/employees");
      }
    } catch {
      setMessage("Login failed");
    } finally {
      setLoading(false);
    }
  };

  const loginGoogle = (res, err) => {
    if (err || !res) setMessage("Login Google failed"); else navigate("/employees");
  };

  return (
    <div className="container" style={{ marginTop: 20 }}>
      <div className="row">
        <div className="card col-md-6 offset-md-3">
          <h3 className="text-center" style={{ margin: 10 }}>
            Login Employee
          </h3>
          <div className="card-body">
            <form>
              <div className="form-group" style={{ marginTop: 20 }}>
                <label htmlFor="emailId">Email Address:</label>
                <input
                  id="emailId"
                  placeholder="Email Address"
                  className="form-control"
                  value={emailId}
                  onChange={(e) => {
                    setEmailId(e.target.value)
                    setMessage("")
                  }}
                />
              </div>
              <div className="form-group" style={{ marginTop: 20 }}>
                <label htmlFor="password">Password:</label>
                <input
                  id="password"
                  type="password"
                  placeholder="Password"
                  className="form-control"
                  value={password}
                  onChange={(e) => {
                    setPassword(e.target.value)
                    setMessage("")
                  }}
                />
              </div>

              {message && (
                <p
                  style={{
                    color: loading ? "blue" : "red",
                    marginTop: 10,
                  }}
                >
                  {message}
                </p>
              )}

              <button
                className="btn-positive"
                style={{ marginTop: 10, width: 150 }}
                onClick={login}
                disabled={loading}
              >
                {loading ? "Please wait..." : "Login"}
              </button>

              <button
                hidden="true"
                type="button"
                className="btn-cancel"
                style={{ marginTop: 10, marginLeft: 10 }}
                onClick={loginGoogle}
                disabled={loading}
              >
                Login Google
              </button>

              <LoginGoogleButton style={{ width: 400, margin: "20px auto 0 auto", display: "block" }} onLoginResult={(res, err) => {
                loginGoogle(res, err);
              }} onClick={() => {
                setMessage("");
              }}
              />

            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginEmployeeFunction;

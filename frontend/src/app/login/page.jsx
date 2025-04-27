//src/app/login/page.jsx

"use client";

import React, { useState, useContext } from "react";
import userContext from "../../context/userContext";
import { login } from "../../services/user-service";
import { doLogin } from "../../auth";
import "./login.css";
import { toast } from "react-toastify";
import { useRouter } from "next/navigation";

const Page = () => {
  const userContxtData = useContext(userContext);
  const router = useRouter();

  const [loginDetail, setLoginDetail] = useState({
    username: "",
    password: "",
  });

  const handleChange = (event, field) => {
    let actualValue = event.target.value;
    setLoginDetail({
      ...loginDetail,
      [field]: actualValue,
    });
  };

  const handleReset = () => {
    setLoginDetail({
      username: "",
      password: "",
    });
  };

  const handleFormSubmit = (event) => {
    event.preventDefault();

    // Check if both fields are filled
    if (
      loginDetail.username.trim() === "" ||
      loginDetail.password.trim() === ""
    ) {
      toast.error("Please fill all the fields !!!");
      return;
    }

    // Attempt to log in
    login(loginDetail)
      .then((data) => {
        // Save the data to local storage using the doLogin function
        doLogin(data, () => {
          // Set user context
          userContxtData.setUser({
            data: data.user,
            login: true,
          });

          // Navigate to user dashboard
          router.push("/user/dashboard");
        });
        toast.success("Logged in successfully !!!");
      })
      .catch((error) => {
        if (
          error.response &&
          (error.response.status === 400 || error.response.status === 404)
        ) {
          toast.error(error.response.data.message);
        } else {
          toast.error("Something went wrong !!!");
        }
      });
  };

  const handleForgotPassword = () => {
    if (!loginDetail.username.trim()) {
      toast.error("Please enter your email!");
      return;
    }
    console.log("Email sent to:" + loginDetail.username.trim());
    toast.info("Password reset functionality is being implemented");
  };

  return (
    <div>
      <div className="login-container">
        {/* Login Card */}
        <div className="login-card-container">
          <div className="login-card">
            <h3>Fill Information to Log In</h3>

            <form onSubmit={handleFormSubmit}>
              <div className="login-form-group">
                <label htmlFor="username">Email: </label>
                <input
                  type="username"
                  placeholder="Enter your email"
                  id="username"
                  value={loginDetail.username}
                  onChange={(e) => handleChange(e, "username")}
                />
              </div>

              <div className="login-form-group">
                <label htmlFor="password">Password: </label>
                <input
                  type="password"
                  placeholder="Enter your password"
                  id="password"
                  value={loginDetail.password}
                  onChange={(e) => handleChange(e, "password")}
                />
              </div>

              <div className="forgot-password-container">
                <button
                  type="button"
                  className="forgot-password-link"
                  onClick={handleForgotPassword}
                >
                  Forgot Password?
                </button>
              </div>

              <div className="login-button-container">
                <button className="login-button" type="submit">
                  Log In
                </button>
                <button
                  className="login-reset-button"
                  type="button"
                  onClick={handleReset}
                >
                  Reset
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Page;

//src/services/user-service.js

import { myAxios, privateAxios } from "./helper";

export const signUp = (user, roleId) => {
  return myAxios
    .post(`/auth/register?roleId=${roleId}`, user)
    .then((response) => response.data);
};

export const login = (loginDetail) => {
  console.log(
    "Login API Request hitted in user-service.login with data :",
    loginDetail
  );

  return myAxios.post("/auth/login", loginDetail).then((response) => {
    console.log("Login API Response:", response.data);
    return response.data;
  });
};

export const getUserById = (userId) => {
  return myAxios.get(`/users/${userId}`).then((resp) => resp.data);
};

//update post
export function updateProfile(user, userId) {
  console.log(user);
  return privateAxios.put(`/users/${userId}`, user).then((resp) => resp.data);
}

//update profile image

export const uploadProfileImage = (image, userId) => {
  let formData = new FormData();
  formData.append("image", image);
  return privateAxios
    .post(`/users/user/image/upload/${userId}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((response) => response.data);
};

// Function to get user data from local storage
export const getUserDataFromLocalStorage = () => {
  const data = localStorage.getItem("data");
  return data ? JSON.parse(data).user : null;
};

// Function to check if the user is an admin
export const isAdmin = () => {
  const user = getUserData();
  return user && user.roles.some((role) => role.id === 501);
};

// Function to check if the user is a doctor
export const isStudent = () => {
  const user = getUserData();
  return user && user.roles.some((role) => role.id === 502);
};

// Function to check if the user is a normal user
export const isTeacher = () => {
  const user = getUserData();
  return user && user.roles.some((role) => role.id === 503);
};

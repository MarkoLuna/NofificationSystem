import { createContext, useState, useEffect } from "react";
import { setAuthToken } from "../api/api";
import { jwtDecode } from "jwt-decode";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [user, setUser] = useState(null);

  const handleToken = (jwt) => {
    if (!jwt) return;

    const decoded = jwtDecode(jwt);
    setUser(decoded);

    // auto logout when token expires
    const currentTime = Date.now() / 1000;
    const timeout = (decoded.exp - currentTime) * 1000;

    setTimeout(() => {
      logout();
    }, timeout);
  };

  const login = (jwt) => {
    localStorage.setItem("token", jwt);
    setToken(jwt);
    setAuthToken(jwt);
    handleToken(jwt);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setUser(null);
    setAuthToken(null);
  };

  useEffect(() => {
    if (token) {
      setAuthToken(token);
      handleToken(token);
    }
  }, []);

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  TextField,
  Button,
  Typography,
  Box,
  Paper,
} from "@mui/material";

import Toast from "./Toast";

import API, { AUTH_API } from "../api/api";
import { AuthContext } from "../auth/AuthContext";

const Login = () => {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [toast, setToast] = useState({ open: false, msg: "", type: "success" });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const params = new URLSearchParams();
      params.append("client_id", import.meta.env.VITE_CLIENT_ID);
      params.append("client_secret", import.meta.env.VITE_CLIENT_SECRET);
      params.append("grant_type", "password");
      params.append("username", username);
      params.append("password", password);

      const res = await AUTH_API.post("", params);
      login(res.data.access_token);
      navigate("/dashboard");
    } catch (err) {
      console.error(err);
      setToast({ open: true, msg: "Invalid credentials", type: "error" });
    }
  };

  return (
    <Container maxWidth="sm">
        <Toast
            open={toast.open}
            message={toast.msg}
            severity={toast.type}
            onClose={() => setToast({ ...toast, open: false })}
            />
      <Paper elevation={3} sx={{ padding: 4, marginTop: 10 }}>
        <Typography variant="h5" gutterBottom>
          Login
        </Typography>

        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            label="Username"
            fullWidth
            margin="normal"
            onChange={(e) => setUsername(e.target.value)}
          />

          <TextField
            label="Password"
            type="password"
            fullWidth
            margin="normal"
            onChange={(e) => setPassword(e.target.value)}
          />

          <Button
            variant="contained"
            fullWidth
            sx={{ mt: 2 }}
            type="submit"
          >
            Login
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default Login;
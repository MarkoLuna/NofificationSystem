import { useState, useContext } from "react";
import {
  TextField,
  Button,
  MenuItem,
  Paper,
  Typography,
  Box,
} from "@mui/material";

import Toast from "./Toast";

import API, { setAuthToken } from "../api/api";
import { AuthContext } from "../auth/AuthContext";

const CreateNotification = ({ onSuccess }) => {
  const { token } = useContext(AuthContext);
  const [toast, setToast] = useState({ open: false, msg: "", type: "success" });

  const [form, setForm] = useState({
    channel: "SMS",
    category: "SPORTS",
    message: "",
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setAuthToken(token);

    await API.post("/notifications/send", form);
    setToast({
      open: true,
      msg: "Notification created successfully",
      type: "success",
    });
    setForm({ ...form, message: "" });
    if (onSuccess) onSuccess();
  };

  return (
    <Paper sx={{ p: 3 }}>
      <Toast
        open={toast.open}
        message={toast.msg}
        severity={toast.type}
        onClose={() => setToast({ ...toast, open: false })}
      />
      <Typography variant="h6">Create Notification</Typography>

      <Box component="form" onSubmit={handleSubmit}>
        <TextField
          select
          label="Channel"
          fullWidth
          margin="normal"
          value={form.channel}
          onChange={(e) => setForm({ ...form, channel: e.target.value })}
        >
          <MenuItem value="SMS">SMS</MenuItem>
          <MenuItem value="EMAIL">EMAIL</MenuItem>
          <MenuItem value="PUSH">PUSH</MenuItem>
        </TextField>

        <TextField
          select
          label="Category"
          fullWidth
          margin="normal"
          value={form.category}
          onChange={(e) => setForm({ ...form, category: e.target.value })}
        >
          <MenuItem value="SPORTS">SPORTS</MenuItem>
          <MenuItem value="FINANCE">FINANCE</MenuItem>
          <MenuItem value="MOVIES">MOVIES</MenuItem>
        </TextField>

        <TextField
          label="Message"
          fullWidth
          margin="normal"
          value={form.message}
          onChange={(e) => setForm({ ...form, message: e.target.value })}
        />

        <Button variant="contained" type="submit" sx={{ mt: 2 }}>
          Create
        </Button>
      </Box>
    </Paper>
  );
};

export default CreateNotification;
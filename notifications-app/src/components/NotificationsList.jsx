import { useEffect, useState, useContext } from "react";
import {
  Typography,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
} from "@mui/material";

import API, { setAuthToken } from "../api/api";
import { AuthContext } from "../auth/AuthContext";

const NotificationsList = ({ refresh }) => {
  const { token } = useContext(AuthContext);
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    if (token) {
      setAuthToken(token);
      fetchNotifications();
    }
  }, [token, refresh]);

  const fetchNotifications = async () => {
    const res = await API.get("/notifications/my");
    setNotifications(res.data);
  };

  return (
    <Paper sx={{ p: 2, mt: 4 }}>
      <Typography variant="h6">Notifications</Typography>

      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Channel</TableCell>
            <TableCell>Category</TableCell>
            <TableCell>Message</TableCell>
          </TableRow>
        </TableHead>

        <TableBody>
          {notifications.map((n, i) => (
            <TableRow key={i}>
              <TableCell>{n.channel}</TableCell>
              <TableCell>{n.category}</TableCell>
              <TableCell>{n.message}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
};

export default NotificationsList;
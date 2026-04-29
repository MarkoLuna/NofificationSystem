import { Container, Typography, Box } from "@mui/material";
import { useState } from "react";
import NotificationsList from "../components/NotificationsList";
import CreateNotification from "../components/CreateNotification";
import Navbar from "../components/Navbar";

const Dashboard = () => {
  const [refresh, setRefresh] = useState(0);

  const handleRefresh = () => {
    setRefresh((prev) => prev + 1);
  };

  return (
    <>
      <Navbar />

      <Container>
        <Typography variant="h4" sx={{ mt: 4 }}>
          Dashboard
        </Typography>

        <Box sx={{ mt: 3 }}>
          <CreateNotification onSuccess={handleRefresh} />
          <NotificationsList refresh={refresh} />
        </Box>
      </Container>
    </>
  );
};

export default Dashboard;
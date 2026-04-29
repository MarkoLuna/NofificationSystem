import { render, screen, fireEvent } from "@testing-library/react";
import Navbar from "./Navbar";
import { AuthContext } from "../auth/AuthContext";
import { BrowserRouter } from "react-router-dom";
import { vi, describe, it, expect } from "vitest";

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe("Navbar Component", () => {
  const mockLogout = vi.fn();

  const renderNavbar = (user = null) => {
    return render(
      <AuthContext.Provider value={{ logout: mockLogout, user }}>
        <BrowserRouter>
          <Navbar />
        </BrowserRouter>
      </AuthContext.Provider>
    );
  };

  it("should render app title", () => {
    renderNavbar();
    expect(screen.getByText(/Notifications App/i)).toBeInTheDocument();
  });

  it("should display default user name when no user is provided", () => {
    renderNavbar();
    expect(screen.getByText("User")).toBeInTheDocument();
  });

  it("should display preferred_username if available", () => {
    renderNavbar({ preferred_username: "JohnDoe" });
    expect(screen.getByText("JohnDoe")).toBeInTheDocument();
  });

  it("should display sub if preferred_username is not available", () => {
    renderNavbar({ sub: "UserID123" });
    expect(screen.getByText("UserID123")).toBeInTheDocument();
  });

  it("should call logout and navigate on logout button click", () => {
    renderNavbar();
    fireEvent.click(screen.getByRole("button", { name: /Logout/i }));
    expect(mockLogout).toHaveBeenCalled();
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });
});

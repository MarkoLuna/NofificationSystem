import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Login from "./Login";
import { AuthContext } from "../auth/AuthContext";
import { BrowserRouter } from "react-router-dom";
import { vi, describe, it, expect, beforeEach, afterEach } from "vitest";
import { AUTH_API } from "../api/api";

// Mock AUTH_API
vi.mock("../api/api", () => ({
  AUTH_API: {
    post: vi.fn(),
  },
  default: {},
}));

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe("Login Component", () => {
  const mockLogin = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  const renderLogin = () => {
    return render(
      <AuthContext.Provider value={{ login: mockLogin }}>
        <BrowserRouter>
          <Login />
        </BrowserRouter>
      </AuthContext.Provider>
    );
  };

  it("should render login form", () => {
    renderLogin();
    expect(screen.getByText(/Login/i, { selector: 'h5' })).toBeInTheDocument();
    expect(screen.getByLabelText(/Username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Password/i)).toBeInTheDocument();
  });

  it("should call login and navigate on successful submission", async () => {
    AUTH_API.post.mockResolvedValue({ data: { access_token: "fake-token" } });
    renderLogin();

    fireEvent.change(screen.getByLabelText(/Username/i), { target: { value: "user" } });
    fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: "pass" } });
    fireEvent.click(screen.getByRole("button", { name: /Login/i }));

    await waitFor(() => {
      expect(AUTH_API.post).toHaveBeenCalled();
      expect(mockLogin).toHaveBeenCalledWith("fake-token");
      expect(mockNavigate).toHaveBeenCalledWith("/dashboard");
    });
  });

  it("should show error toast on failed submission", async () => {
    const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});
    AUTH_API.post.mockRejectedValue(new Error("Login failed"));
    renderLogin();

    fireEvent.change(screen.getByLabelText(/Username/i), { target: { value: "user" } });
    fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: "pass" } });
    fireEvent.click(screen.getByRole("button", { name: /Login/i }));

    await waitFor(() => {
      expect(screen.getByText(/Invalid credentials/i)).toBeInTheDocument();
    });
  });
});

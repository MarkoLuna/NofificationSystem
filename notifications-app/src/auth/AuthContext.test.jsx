import { render, screen, act } from "@testing-library/react";
import { AuthProvider, AuthContext } from "./AuthContext";
import { useContext } from "react";
import { vi, describe, it, expect, beforeEach } from "vitest";
import * as api from "../api/api";
import { jwtDecode } from "jwt-decode";

vi.mock("jwt-decode", () => ({
  jwtDecode: vi.fn(),
}));

vi.mock("../api/api", () => ({
  setAuthToken: vi.fn(),
  default: {
    defaults: {
      headers: {
        common: {},
      },
    },
  },
}));

const TestComponent = () => {
  const { token, user, login, logout } = useContext(AuthContext);
  return (
    <div>
      <div data-testid="token">{token}</div>
      <div data-testid="user">{user ? user.sub : "no user"}</div>
      <button onClick={() => login("fake-jwt")}>Login</button>
      <button onClick={logout}>Logout</button>
    </div>
  );
};

describe("AuthContext", () => {
  beforeEach(() => {
    localStorage.clear();
    vi.clearAllMocks();
  });

  it("should initialize with null token and user", () => {
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    expect(screen.getByTestId("token").textContent).toBe("");
    expect(screen.getByTestId("user").textContent).toBe("no user");
  });

  it("should login and set token", () => {
    const mockUser = { sub: "testuser", exp: Math.floor(Date.now() / 1000) + 3600 };
    jwtDecode.mockReturnValue(mockUser);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    act(() => {
      screen.getByText("Login").click();
    });

    expect(localStorage.getItem("token")).toBe("fake-jwt");
    expect(screen.getByTestId("token").textContent).toBe("fake-jwt");
    expect(screen.getByTestId("user").textContent).toBe("testuser");
    expect(api.setAuthToken).toHaveBeenCalledWith("fake-jwt");
  });

  it("should logout and clear token", () => {
    localStorage.setItem("token", "existing-jwt");
    const mockUser = { sub: "existinguser", exp: Math.floor(Date.now() / 1000) + 3600 };
    jwtDecode.mockReturnValue(mockUser);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    act(() => {
      screen.getByText("Logout").click();
    });

    expect(localStorage.getItem("token")).toBeNull();
    expect(screen.getByTestId("token").textContent).toBe("");
    expect(screen.getByTestId("user").textContent).toBe("no user");
    expect(api.setAuthToken).toHaveBeenCalledWith(null);
  });
});

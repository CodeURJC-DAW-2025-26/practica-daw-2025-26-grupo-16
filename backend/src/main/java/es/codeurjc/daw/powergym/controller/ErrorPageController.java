package es.codeurjc.daw.powergym.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        HttpStatus status = HttpStatus.resolve(statusCode);

        model.addAttribute("status", statusCode);
        model.addAttribute("error", status != null ? status.getReasonPhrase() : "Error");
        model.addAttribute("message", getErrorMessage(statusCode));

        return "error";
    }

    private String getErrorMessage(int statusCode) {
        return switch (statusCode) {
            case 400 -> "The request could not be understood or was malformed.";
            case 403 -> "You do not have permission to access this resource.";
            case 404 -> "The requested resource was not found.";
            case 405 -> "The HTTP method is not allowed for this resource.";
            case 500 -> "An internal server error occurred. Please try again later.";
            default -> "An unexpected error occurred. Please try again later.";
        };
    }
}

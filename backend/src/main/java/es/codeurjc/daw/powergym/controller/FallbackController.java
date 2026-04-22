/*package es.codeurjc.daw.powergym.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FallbackController {

	@RequestMapping("/**")
	public String handleNotFound(HttpServletRequest request, Model model) {
		
		String requestPath = (String) request.getAttribute("javax.servlet.forward.request_uri");
		if (requestPath == null) {
			requestPath = request.getRequestURI();
		}

		// Check if it's a static resource request (has extension)
		if (requestPath.matches(".*\\.[a-zA-Z0-9]+$")) {
			// Let static resources be handled normally
			return "forward:" + requestPath;
		}

		// Map to error page
		model.addAttribute("status", 404);
		model.addAttribute("error", "Not Found");
		model.addAttribute("message", "The requested resource was not found.");

		return "error";
	}
}*/

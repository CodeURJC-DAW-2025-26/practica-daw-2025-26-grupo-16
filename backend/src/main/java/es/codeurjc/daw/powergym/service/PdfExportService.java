package es.codeurjc.daw.powergym.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.Training;

@Service
public class PdfExportService {

	public byte[] buildNutritionPdf(Nutrition nutrition) {
		Document document = new Document();
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, output);
			document.open();

			addTitle(document, "Nutrition Details");
			addField(document, "Name", nutrition.getName());
			addField(document, "Calories", nutrition.getCalories() + " kcal");
			addField(document, "Goal", nutrition.getGoal());
			addField(document, "Meals of the Day", nutrition.getDescription());

			return output.toByteArray();
		} catch (Exception exception) {
			throw new IllegalStateException("Error generating nutrition PDF", exception);
		} finally {
			document.close();
		}
	}

	public byte[] buildTrainingPdf(Training training) {
		Document document = new Document();
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, output);
			document.open();

			addTitle(document, "Training Details");
			addField(document, "Name", training.getName());
			addField(document, "Duration", training.getTime() + " minutes");
			addField(document, "Goal", training.getGoal());
			addField(document, "Exercises", training.getDescription());

			return output.toByteArray();
		} catch (Exception exception) {
			throw new IllegalStateException("Error generating training PDF", exception);
		} finally {
			document.close();
		}
	}

	private void addTitle(Document document, String title) {
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
		document.add(new Paragraph(title, titleFont));
		document.add(new Paragraph(" "));
	}

	private void addField(Document document, String label, String value) {
		Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
		Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

		document.add(new Paragraph(label + ":", labelFont));
		document.add(new Paragraph(value != null ? value : "-", valueFont));
		document.add(new Paragraph(" "));
	}
}

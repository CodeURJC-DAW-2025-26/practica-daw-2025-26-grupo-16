package es.codeurjc.daw.powergym.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Image;

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
			if (nutrition.getImage() != null) {

				java.sql.Blob blob = nutrition.getImage().getImageFile();
				byte[] imageBytes = blobToBytes(blob);

				addImage(document, imageBytes);
			}
			addField(document, "Name", nutrition.getName());
			addField(document, "Calories", nutrition.getCalories() + " kcal");
			addField(document, "Goal", nutrition.getGoal());
			addField(document, "Meals of the Day", nutrition.getDescription());

			document.close();
			return output.toByteArray();
		} catch (Exception exception) {
			throw new IllegalStateException("Error generating nutrition PDF", exception);
		}
	}

	public byte[] buildTrainingPdf(Training training) {
		Document document = new Document();
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, output);
			document.open();

			addTitle(document, "Training Details");
			if (training.getImage() != null) {

				java.sql.Blob blob = training.getImage().getImageFile();
				byte[] imageBytes = blobToBytes(blob);

				addImage(document, imageBytes);
			}
			addField(document, "Name", training.getName());
			addField(document, "Duration", training.getTime() + " minutes");
			addField(document, "Goal", training.getGoal());
			addField(document, "Exercises", training.getDescription());

			document.close();

			return output.toByteArray();
		} catch (Exception exception) {
			throw new IllegalStateException("Error generating training PDF", exception);
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

	private void addImage(Document document, byte[] imageBytes) {
		try {
			if (imageBytes != null && imageBytes.length > 0) {

				com.lowagie.text.Image pdfImage =
						com.lowagie.text.Image.getInstance(imageBytes);

				pdfImage.scaleToFit(400, 300);
				pdfImage.setAlignment(com.lowagie.text.Image.ALIGN_CENTER);

				document.add(pdfImage);
				document.add(new Paragraph(" "));
			}
		} catch (Exception e) {
			throw new IllegalStateException("Error adding image to PDF", e);
		}
	}

	private byte[] blobToBytes(java.sql.Blob blob) {
		try {
			if (blob == null) return null;
			return blob.getBytes(1, (int) blob.length());
		} catch (Exception e) {
			throw new IllegalStateException("Error converting Blob to byte[]", e);
		}
	}
}

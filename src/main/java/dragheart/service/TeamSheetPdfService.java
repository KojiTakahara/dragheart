package dragheart.service;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Component;

@Component
public class TeamSheetPdfService extends PdfService {

	private static final int TEAM_NAME_X = 90;
	private static final double TEAM_Y = 144;
	private static final double SEAT_A = 479.8;
	private static final double SEAT_B = 508.1;
	private static final double SEAT_C = 536.6;

	public TeamSheetPdfService() {
		super.LEFT = 59;
		super.START_MAIN = 718;
    super.START_GR = 349;
		super.START_HYPER = 226;
		super.LINE_SPACE = 16.4;
		super.NAME_Y = 80;
		super.NAME_KANA_Y = 112;
		super.ID_X = 45;
		super.DECK_ON_X = 511;
		super.DECK_OFF_X = 533.5;
		super.DECK_Y = 15.1;
		super.FORBIDDENSTAR_ON_X = 245.5;
		super.FORBIDDENSTAR_OFF_X = 267.8;
		super.FORBIDDENSTAR_Y = 388.2;
	}
	
	public void writeTeamName(PDPageContentStream contentStream, PDFont font, String name) {
		if (name == null) {
			return;
		}
		this.writeText(contentStream, font, name, TEAM_NAME_X, (float) TEAM_Y);
	}

	public void writeSeat(PDPageContentStream contentStream, PDFont font, String seat) {
		if (seat == null) {
			return;
		}
		switch (seat) {
			case "A":
				this.writeCircle(contentStream, font, (float) SEAT_A, (float) (TEAM_Y - 1.4));
				break;
			case "B":
				this.writeCircle(contentStream, font, (float) SEAT_B, (float) (TEAM_Y - 1.4));
				break;
			case "C":
				this.writeCircle(contentStream, font, (float) SEAT_C, (float) (TEAM_Y - 1.4));
				break;
			default:
				break;
		}
	} 
}
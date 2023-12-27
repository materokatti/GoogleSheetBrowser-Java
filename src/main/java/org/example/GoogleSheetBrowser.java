package org.example;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleSheetBrowser {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        // 서비스 계정 키 파일 경로
        String keyFilePath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        // Google Sheets API 클라이언트 서비스 초기화
        Sheets sheetsService = getSheetsService(keyFilePath);

        // 스프레드시트 ID 및 범위
        String spreadsheetId = "1b3az54K2-P1BEB0IVdQ4Ces9a3NHsrlKqtxzeJuJnU0";
        String range = "Sheet1!A1:D";

        // 데이터 읽기
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                // 각 행의 데이터 출력
                System.out.println(row);
            }
        }
    }

    private static Sheets getSheetsService(String keyFilePath) throws IOException, GeneralSecurityException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        FileInputStream serviceAccountStream = new FileInputStream(keyFilePath);

        // 인증 정보 설정
        GoogleCredential credential = GoogleCredential.fromStream(serviceAccountStream)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS_READONLY));

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName("Google Sheets Example")
                .build();
    }
}

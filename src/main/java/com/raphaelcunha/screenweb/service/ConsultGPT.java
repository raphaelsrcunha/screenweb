package com.raphaelcunha.screenweb.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultGPT {
    public static String getTranslation(String text) {
        OpenAiService service = new OpenAiService("x");

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var response = service.createCompletion(requisicao);
        return response.getChoices().get(0).getText();
    }
}

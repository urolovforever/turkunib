package com.tiu.turk.batch.io.common.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiu.turk.batch.io.common.dto.OpenAiTranslationResult;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import java.util.HashMap;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class OpenAiTranslateProcessor
implements ItemProcessor<SourcePayload, TranslationResult> {
    private final ChatClient chatClient;

    public OpenAiTranslateProcessor(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new Advisor[]{new SimpleLoggerAdvisor()}).build();
    }

    public TranslationResult process(SourcePayload item) throws Exception {
        String promptTemplate1 = "    SYSTEM:\n    You are a professional *deterministic* translator. You translate English content accurately and naturally.\n    Follow the output contract strictly. Never include explanations or extra text.\n\n    CONTRACT (STRICT):\n    - Return ONLY one valid JSON object and nothing else.\n    - Standard JSON only: double quotes, no comments, no trailing commas, no backticks.\n    - Output MUST be exactly this structure:\n      {prompt_json_example}\n    - If an input field is missing or null, output an empty string \"\" for that field.\n    - Do NOT translate or alter code blocks, HTML tags, URLs, or template placeholders. Translate only human-readable text.\n    - If the source text is already in the target language, still return a natural version in that language (normalize grammar).\n    - If input is empty or only whitespace, return {prompt_json_example_empty}.\n    - Return minified JSON (no newlines or extra spaces).\n\n    LANGUAGE:\n    - Human name: {language_name}\n    - Language code: {language_code}\n\n    INPUT FORMAT:\n    - Input is a single JSON object with optional string fields: {fields}.\n    - Example:\n    {prompt_json_example}\n\n    TASK:\n    Translate the {fields} from English into the target language {language_name}, following the rules above.\n    Keep meaning and tone; transliterate proper names only when appropriate.\n\n    OUTPUT (ONLY THIS JSON, nothing else):\n    {prompt_json_example}\n\n    BEGIN INPUT JSON\n    {text}\n    END INPUT JSON\n";
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplate1);
        Prompt prompt = promptTemplate.create(Map.of("language_name", item.targetLocale().getLanguage(), "language_code", item.targetLocale().getIsoCode(), "fields", String.join((CharSequence)", ", item.fieldsData().keySet()), "prompt_json_example", this.getNormalizedJson(this.setValues(item.fieldsData(), "...")), "prompt_json_example_empty", this.getNormalizedJson(this.setValues(item.fieldsData(), "")), "text", this.getNormalizedJson(item.fieldsData())));
        OpenAiTranslationResult translationResult = (OpenAiTranslationResult)this.chatClient.prompt(prompt).call().entity(OpenAiTranslationResult.class);
        return new TranslationResult(item.taskId(), item.entityId(), item.sourceHash(), item.module(), item.sourceLocale(), item.targetLocale(), translationResult.translations());
    }

    private String getNormalizedJson(Map<String, String> fields) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(Map.of("translations", fields));
    }

    private Map<String, String> setValues(Map<String, String> fields, String val) {
        HashMap<String, String> copy = new HashMap<String, String>(fields);
        for (String key : fields.keySet()) {
            copy.put(key, val);
        }
        return copy;
    }
}


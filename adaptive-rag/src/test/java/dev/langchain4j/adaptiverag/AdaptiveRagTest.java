package dev.langchain4j.adaptiverag;

import dev.langchain4j.DotEnvConfig;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdaptiveRagTest {

    @BeforeAll
    public static void beforeAll() throws Exception {
        FileInputStream configFile = new FileInputStream("logging.properties");
        LogManager.getLogManager().readConfiguration(configFile);

        DotEnvConfig.load();


    }

    @Test
    public void QuestionRewriterTest() {
        String openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        String result = QuestionRewriter.of(openApiKey).apply("agent memory");
        assertEquals("What is the role of memory in an agent's functioning?", result);
    }

    @Test
    public void RetrievalGraderTest() {

        String openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        RetrievalGrader grader = RetrievalGrader.of(openApiKey);

        ChromaEmbeddingStore chroma = new ChromaEmbeddingStore(
                "http://localhost:8000",
                "rag-chroma",
                Duration.ofMinutes(2) );
        OpenAiEmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(openApiKey)
                .build();

        String question = "agent memory";
        Embedding queryEmbedding = embeddingModel.embed(question).content();

        EmbeddingSearchRequest query = EmbeddingSearchRequest.builder()
                .queryEmbedding( queryEmbedding )
                .maxResults( 1 )
                .minScore( 0.0 )
                .build();
        EmbeddingSearchResult<TextSegment> relevant = chroma.search( query );

        List<EmbeddingMatch<TextSegment>> matches = relevant.matches();

        assertEquals( 1, matches.size() );

        RetrievalGrader.DocumentScore answer =
                grader.apply( new RetrievalGrader.Arguments(question, matches.get(0).embedded().text()));

        assertEquals( "no", answer.binaryScore);


    }

    @Test
    public void WebSearchTest() {

        String tavilyApiKey = DotEnvConfig.valueOf("TAVILY_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        WebSearchTool webSearchTool = WebSearchTool.of(tavilyApiKey);
        List<Content> webSearchResults = webSearchTool.apply("agent memory");

        System.out.println( webSearchResults );

    }

}

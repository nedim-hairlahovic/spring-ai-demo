package dev.nhairlahovic.springai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Configuration
public class VectorStoreConfig {

    private final String vectorStoreName = "vectorStore.json";

    @Value("classpath:HR_Policies.pdf")
    private Resource policyFile;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        var vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        File vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {
            log.info("Loading vector store from {}", vectorStoreFile.getAbsolutePath());
            vectorStore.load(vectorStoreFile);
        } else {
            log.info("Creating vector store from {}", vectorStoreFile.getAbsolutePath());

            var tikaDocumentReader = new TikaDocumentReader(policyFile);
            List<Document> docs = tikaDocumentReader.get();
            TextSplitter textSplitter = TokenTextSplitter.builder().withChunkSize(200).withMaxNumChunks(400).build();
            vectorStore.add(textSplitter.split(docs));

            vectorStore.save(vectorStoreFile);
        }

        return vectorStore;
    }

    private File getVectorStoreFile() {
        var path = Paths.get("src/main/resources/data/" + vectorStoreName);
        File file = path.toFile();
//        file.getParentFile().mkdirs();

        return file;
    }
}

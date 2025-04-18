# Environment Variables Configuration for Culina AI Backend

To run this application locally, you need to set up the following environment variables:

## Required Environment Variables

```
# Google OAuth Credentials
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google

# OpenAI API Key
OPENAI_API_KEY=your-openai-api-key
```

## Setting Up Environment Variables

### Option 1: Environment Variables in your OS

#### For macOS/Linux:
```bash
export GOOGLE_CLIENT_ID=your-google-client-id
export GOOGLE_CLIENT_SECRET=your-google-client-secret
export OPENAI_API_KEY=your-openai-api-key
```

#### For Windows:
```cmd
set GOOGLE_CLIENT_ID=your-google-client-id
set GOOGLE_CLIENT_SECRET=your-google-client-secret
set OPENAI_API_KEY=your-openai-api-key
```

### Option 2: Using application-local.properties (Recommended for Development)

Create a file called `application-local.properties` in the `src/main/resources` directory with the following content:

```properties
spring.security.oauth2.client.registration.google.client-id=your-google-client-id
spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret
spring.ai.openai.api-key=your-openai-api-key
```

Then run your application with the `local` profile:
```bash
./mvnw spring-boot:run -Dspring.profiles.active=local
```

**IMPORTANT: Never commit your actual API keys or secrets to the repository. The `application-local.properties` file is included in .gitignore to prevent accidental commits.**
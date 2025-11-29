# API Key Setup Instructions

## Secure Google Maps API Key Configuration

This project uses the **Secrets Gradle Plugin** to keep API keys secure and out of version control.

### For New Developers

1. **Copy the default secrets file:**
   ```bash
   cp secrets.defaults.properties secrets.properties
   ```

2. **Add your Google Maps API key:**
   Open `secrets.properties` and replace `YOUR_MAPS_API_KEY_HERE` with your actual API key:
   ```properties
   MAPS_API_KEY=your_actual_key_here
   ```

3. **Build the project:**
   The plugin will automatically inject the API key into your app during build time.

### How It Works

- **secrets.properties** - Contains your actual API keys (git-ignored, never commit this!)
- **secrets.defaults.properties** - Template with placeholder values (safe to commit)
- The Secrets Gradle Plugin reads `secrets.properties` and injects values into `AndroidManifest.xml`
- The API key is referenced as `${MAPS_API_KEY}` in the manifest

### Security Notes

✅ **DO:**
- Keep your `secrets.properties` file local only
- Share `secrets.defaults.properties` with the team
- Get your own Google Maps API key from [Google Cloud Console](https://console.cloud.google.com/)

❌ **DON'T:**
- Never commit `secrets.properties` to git
- Never hardcode API keys in source files
- Never share your API keys in chat or documentation

### Troubleshooting

If you get a build error about missing MAPS_API_KEY:
1. Ensure `secrets.properties` exists in the project root
2. Verify it contains a valid `MAPS_API_KEY=...` line
3. Run `./gradlew clean` and rebuild

### Getting Your Own API Key

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the **Maps SDK for Android** API
4. Create credentials (API Key)
5. Restrict the key to Android apps with your package name
6. Copy the key to your `secrets.properties` file

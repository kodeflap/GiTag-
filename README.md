# GiTag: An Android Library for Metadata Management

GiTag is an Android library built using Kotlin to extract and erase image metadata, returning results as JSON. The library serves two primary purposes:
1. **Extract Metadata**: Retrieve metadata from images and convert it into a JSON format.
2. **Erase Metadata**: Remove metadata from images to protect against the exposure of sensitive information.

## Integration

To integrate GiTag into your Android project, follow these steps:

1. **Add the AAR file**

   Place the [`gitag-release.aar`](https://github.com/kodeflap/GiTag-/tree/main/app/libs) file in the `libs` directory of your project.

   ```gradle
   implementation(files("libs/gitag-release.aar"))
   ```

2. **Add Required Dependencies**

   Add the following dependencies to your `build.gradle` file:

   ```gradle
   implementation("io.coil-kt:coil-compose:2.4.0")
   implementation("com.google.code.gson:gson:2.8.8")
   ```

## Usage

### Extract Metadata

To extract metadata from an image and get the JSON response, use the `extractMetadata` function:

```kotlin
val metadataJson = GitTag.extractMetadata(context, imageUri)
```

### Remove Metadata

To remove metadata from an image, use the `removeMetadata` function. This function will return the URI of the image with the metadata removed:

```kotlin
val updatedUri = GitTag.removeMetadata(context, imageUri)
```

### Extract Other Exif Attributes

To extract additional Exif attributes, use the `extractOtherExifAttributes` function. This is useful for getting detailed metadata:

```kotlin
val otherMetadata = GitTag.extractOtherExifAttributes(exifInterface)
```

## Tech Stack

- **Kotlin**
- **Android SDK**: Minimum 24, Maximum 34
- **Dependencies**:
  - Coil: For image loading
  - Gson: For JSON parsing

## Contribution

Contributions are welcome! If you have suggestions or improvements, please feel free to submit a pull request or open an issue.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

```

This Markdown documentation includes headings, code snippets, and instructions for integrating and using the GiTag library, as well as details about contributing and licensing.

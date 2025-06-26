# Greeting Card Background Images

This directory should contain the background images for the greeting card feature.

## Required Images

Please add the following images to this directory:

1. **background1.jpg** - Background for "Background 1" theme
2. **background2.jpg** - Background for "Background 2" theme  
3. **background3.jpg** - Background for "Background 3" theme
4. **background4.jpg** - Background for "Background 4" theme
5. **default.jpg** - Default background (fallback)

## Image Specifications

- **Format**: JPG or PNG
- **Recommended Size**: 400x300 pixels (4:3 aspect ratio)
- **File Size**: Keep under 1MB per image for optimal performance
- **Quality**: High quality, suitable for greeting card backgrounds

## Suggested Themes

- **Background 1**: Romantic/Valentine theme (roses, hearts, etc.)
- **Background 2**: Birthday celebration theme (balloons, confetti, etc.)
- **Background 3**: Congratulations theme (achievement, success, etc.)
- **Background 4**: Holiday/Seasonal theme (Christmas, Easter, etc.)

## Implementation Notes

The images will be automatically loaded by the GreetingCardPreviewController when users preview their greeting cards. The system will fall back to a default background color if images are not found.

## File Structure

```
client/src/main/resources/images/greeting_cards/
├── README.md
├── background1.jpg
├── background2.jpg
├── background3.jpg
├── background4.jpg
└── default.jpg
``` 
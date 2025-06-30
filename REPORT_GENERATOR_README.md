# 📊 Report Generator - Lilach Flowers

## Overview
The Report Generator is a comprehensive analytics tool designed for Lilach Flowers employees to generate detailed reports about store performance, sales, and customer data.

## Features

### 🎯 Core Functionality
- **Date Range Selection**: Choose custom date ranges or use quick presets
- **Multiple Report Types**: 
  - 📦 Orders Summary
  - 💰 Revenue Analysis  
  - 🌹 Popular Products
  - 👥 Customer Analysis
  - ⚠️ Complaints & Refunds

### 📈 Visual Analytics
- **Line Charts**: Revenue trends over time
- **Pie Charts**: Product distribution and customer segments
- **Real-time Updates**: Charts update based on selected report type
- **Colorful Design**: Matches the project's lilac theme

### 📊 Summary Statistics
- Total Orders
- Total Revenue
- Average Order Value
- Top Performing Product

### 💾 Export Options
- **Text Report**: Simple text-based export (.txt)
- **CSV Export**: Structured data for Excel analysis
- **Email Reports**: Future feature (placeholder)

## How to Use

### 1. Access the Report Generator
1. Log in as an employee
2. Navigate to the employee catalog
3. Click the "📊 Reports" button in the header

### 2. Select Date Range
- Use the date pickers to select start and end dates
- Click "This Month" for quick preset
- Click "Generate Report" to load data

### 3. Choose Report Type
Select from the available radio buttons:
- **Orders Summary**: General order statistics
- **Revenue Analysis**: Financial performance
- **Popular Products**: Product sales analysis
- **Customer Analysis**: Customer segment data
- **Complaints & Refunds**: Issue tracking

### 4. View Charts
- **Left Chart**: Revenue trend line chart
- **Right Chart**: Distribution pie chart
- Charts automatically update based on report type

### 5. Export Data
- **📄 Export Report**: Save as text file
- **📊 Export CSV**: Save as CSV for Excel
- **📧 Email Report**: Future feature
- **🔄 Refresh**: Reload current data

## Technical Details

### Files Created/Modified
- `report_generator.fxml` - Main UI layout
- `ReportGeneratorController.java` - Main controller logic
- `ChartUtils.java` - Chart generation utilities
- `PDFExporter.java` - Export functionality
- `catalog_employee.fxml` - Added Reports button
- `CatalogController_employee.java` - Added openReports method

### Dependencies
- JavaFX Charts for visualization
- ControlsFX for DatePicker
- EventBus for communication
- Standard Java libraries for file I/O

### Sample Data
The current implementation uses sample data for demonstration:
- Revenue data with seasonal variation
- Product sales with realistic distributions
- Customer segments and complaint data
- Branch performance metrics

## Future Enhancements

### Planned Features
1. **Real Database Integration**: Replace sample data with actual database queries
2. **Advanced PDF Export**: Professional PDF reports with embedded charts
3. **Email Integration**: Send reports via email
4. **More Chart Types**: Bar charts, area charts, etc.
5. **Filtering Options**: Filter by branch, product category, etc.
6. **Scheduled Reports**: Automatic report generation
7. **Interactive Charts**: Clickable chart elements

### Data Integration Points
When ready to connect real data, modify these methods in `ReportGeneratorController.java`:
- `loadSampleData()` - Replace with database queries
- `generateReport()` - Add actual data fetching logic
- `updateChartsForReportType()` - Connect to real data sources

## Styling
The report generator follows the project's design patterns:
- **Primary Color**: #C8A2C8 (Lilac)
- **Background**: Light gray (#f8f9fa)
- **Borders**: Rounded corners with lilac accents
- **Typography**: Segoe UI font family
- **Icons**: Emoji icons for visual appeal

## Troubleshooting

### Common Issues
1. **Charts not displaying**: Ensure JavaFX Charts dependency is included
2. **Date picker not working**: Verify ControlsFX dependency
3. **Export errors**: Check file permissions and path validity

### Performance Notes
- Sample data generation is optimized for smooth UI experience
- Charts are animated for better user experience
- Large datasets should be paginated in future implementations

## Contributing
To add new report types or chart functionality:
1. Add new radio button in FXML
2. Create corresponding chart update method
3. Add sample data generation in ChartUtils
4. Update the switch statement in `updateChartsForReportType()`

---

**Note**: This is a skeleton implementation with sample data. Real data integration will be implemented in future iterations. 
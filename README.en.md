# Jsjds2024

[Try me](http://47.93.44.153/)

#### Introduction
- 2024(17th)Chinese Collegiate Computing Competition
- Project Name: Smart Library(The Design and Implementation of a Smart Library System Based on SpringBoot and Vue)
- Project Background:
    - In today's society, with the rapid development of technology, e-books have become an important part of the reading culture. In contrast, physical books seem to be gradually losing their place in people's lives, with many books accumulating dust on shelves without ever reaching readers. However, there is still a large group of readers who are dedicated to traditional paper books, believing that physical books provide a more direct and genuine reading experience, such as the feel of turning pages and the smell of paper, which e-books cannot replace.

    - Nevertheless, despite the enduring love for physical books, many practical constraints make it difficult for people to enjoy reading physical books. Especially with the busy pace of life and increasing living costs, visiting libraries in person to borrow and return books has become inconvenient. Constraints such as library locations, operating hours, and complicated borrowing processes can be obstacles for people wanting to read physical books.

    - Against this backdrop, we recognized the importance of creating a platform that can bridge the gap between traditional paper books and the convenience of digital access. Our goal is to provide a more convenient solution for people who still love and insist on reading paper books, enabling them to borrow and enjoy physical books without being restricted by time or space. By integrating online services with physical books, we aim to create a new reading ecosystem that not only meets people's emotional needs for physical books but also embraces the convenience of modern life.

- Project Overview:
    - This project aims to develop an integrated library resource management and service platform called "Smart Library." By leveraging technology, this platform integrates resources from multiple offline libraries to solve traditional library service issues such as access restrictions, scattered resources, and cumbersome processes. The Smart Library system provides a flexible borrowing and returning mechanism, allowing readers to choose the most convenient way to borrow and return books according to their needs, whether by visiting the library in person or using courier services.

    - Additionally, the system introduces an advanced management interface that allows library administrators to efficiently review borrowing requests and monitor book status. After borrowing, readers can provide feedback on services and book quality through satisfaction surveys and rating mechanisms, continuously improving library services.

    - Another core feature of the system is the use of data visualization technologies and machine learning algorithms to support efficient information management and intelligent decision-making. By monitoring and analyzing key metrics such as borrowing volume, reader numbers, and satisfaction levels in real-time, Smart Library provides scientific decision support for library administrators. At the same time, readers can easily view their borrowing history and preferences through the system interface, further enhancing the user experience.

    - In summary, the Smart Library project aims to enhance library service accessibility, convenience, and personalization by integrating and optimizing offline library resources with modern information technology. This project aspires to create a seamless, efficient, and personalized borrowing environment for readers while providing strong data support and service quality monitoring capabilities for library management.

#### Software Architecture
##### Backend Technologies
- SpringBoot
- Spring Security
- JWT
- MyBatis
- Druid
- Fastjson

##### Frontend Technologies
- Vue
- Vuex
- Element-ui
- Apache ECharts
- Axios
- Sass
- Quill

#### Installation Guide

1. Enter the project directory:
   ```bash
   cd ruoyi-ui
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the service:
   ```bash
   npm run dev
   ```

#### Usage Instructions

1. Resolve `Node.js` encryption library compatibility issues:
    - Set `Node.js` encryption policy. Before running the build script, set in the terminal:
   ```bash
   set NODE_OPTIONS=--openssl-legacy-provider
   ```
2. Resolve the issue where `URL` does not automatically add a port during file upload (when deploying to a server):
    - Modify the file path `profile` in `application.yml` to:
   ```yaml
   /home/ruoyi/uploadPath
   ```
    - Modify the `fileName` in the common upload request (for multiple files) interface of `CommonController.java` to:
   ```java
   "/prod-api" + FileUploadUtils.upload(filePath, file);
   ```

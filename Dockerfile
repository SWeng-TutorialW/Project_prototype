FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

# מעתיקים את כל הקוד (כולל ה־pom הראשי)
COPY . .

# בונים את כל המודולים (כולל parent, server, entities, client)
RUN mvn clean install

# נכנסים לשרת ומריצים אותו
WORKDIR /app/server
CMD ["mvn", "exec:java"]
#!/bin/bash
# מחכה עד שמסד הנתונים מגיב

until mysql -h "mysql" -u "root" -p"201234" -e "SELECT 1;" > /dev/null 2>&1; do
  echo "⏳ ממתין שה-MySQL יעלה..."
  sleep 2
done

echo "✅ MySQL זמין, מפעיל את השרת..."
exec "$@"

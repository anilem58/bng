# Bliss and Glow — Coursework Starter

**Module:** CS5054NP — Advanced Programming and Technologies
**Project:** Bliss and Glow — Online Beauty & Cosmetics Web Application
**Tech stack:** Java EE / Jakarta Servlets · JSP · MySQL · JDBC · Plain CSS

> 🎯 Tagline: *Beauty. Production. You.*

This zip contains the **starter assets** for the coursework — product images, brand logo, database schema, seed data, and a ready-to-paste prompt for Claude Code that will scaffold the entire web application for you.

---

## 📂 What's in this zip

```
BlissAndGlow-starter/
├── README.md                   ← this file
├── CLAUDE_CODE_PROMPT.md       ← copy this into Claude Code
├── database/
│   ├── schema.sql              ← run first in phpMyAdmin
│   └── seed.sql                ← run second (creates 20 products + admin)
└── src/main/webapp/assets/images/
    ├── logo/bng-logo.png       ← brand logo
    └── products/1..20.jpeg     ← 20 product images already mapped in seed.sql
```

---

## 🚀 How to use this starter

### Step 1 — Install prerequisites
- **JDK 17** (or 11)
- **Apache Tomcat 10** (or 9 if you prefer `javax.*`)
- **XAMPP** (for MySQL + phpMyAdmin)
- **VS Code** or **IntelliJ IDEA Community** with Java + Maven support
- **Claude Code** (the CLI from Anthropic)
- **Git** and a **GitHub account**

### Step 2 — Create the database
1. Start XAMPP and turn on **Apache** and **MySQL**.
2. Open `http://localhost/phpmyadmin`.
3. Click the **SQL** tab and run the contents of `database/schema.sql`.
4. Then run `database/seed.sql`.

You now have a `blissandglow_db` database with 20 products and a default admin.

**Default admin login:**
- Email: `admin@blissandglow.com`
- Password: `Admin@123`

### Step 3 — Generate the project with Claude Code
1. Extract this zip somewhere (e.g. `C:\Users\you\BlissAndGlow`).
2. Open a terminal **in that folder**.
3. Run `claude` (the Claude Code CLI).
4. Open `CLAUDE_CODE_PROMPT.md` and **copy the prompt** between the triple backticks.
5. Paste it into Claude Code and let it work, phase by phase.

Claude Code will create `pom.xml`, all Java packages, JSPs, CSS, filters, etc. — everything needed for the full coursework.

### Step 4 — Build & run
After Claude Code finishes:

```bash
mvn clean package
```

Deploy the resulting `target/BlissAndGlow.war` to Tomcat (`webapps/` folder), then visit:

```
http://localhost:8080/BlissAndGlow/
```

### Step 5 — Push to GitHub (required for submission)
```bash
git init
git add .
git commit -m "Initial commit — Bliss and Glow coursework"
git branch -M main
git remote add origin https://github.com/<your-username>/BlissAndGlow.git
git push -u origin main
```

Make sure the repo is **public** and include the link on your report cover page.

---

## ✅ Coursework checklist (Task A — 50 marks)

| Component | Marks | Status |
|---|---|---|
| Database design (normalized, FK, indexes) | 5 | schema.sql ready ✅ |
| Admin dashboard (CRUD, approvals, reports) | 10 | scaffolded by Claude Code |
| User portal (register, browse, wishlist, order) | 10 | scaffolded by Claude Code |
| Authentication & Authorization (BCrypt, role-based, filter) | 5 | scaffolded by Claude Code |
| MVC architecture (controller / service / dao / model) | 5 | scaffolded by Claude Code |
| Frontend (JSP + CSS, responsive, no Bootstrap) | 5 | scaffolded by Claude Code |
| Additional features (About, Contact) | 5 | scaffolded by Claude Code |
| Validation, exception handling, code style | 5 | scaffolded by Claude Code |

---

## 📝 For Task B (Report — 50 marks)
The report is **your own work** — do not generate it with AI. Use the wireframes, ER diagram, class diagram, test cases, and screenshots from your built system. The coursework brief warns that **contract cheating** and using AI to write the report can lead to module failure. Use Claude Code to help with the **code**, but write the report yourself using your real screenshots and your own analysis.

---

## ⚠️ Important reminders from the coursework brief
- The Library Management System example in the brief **cannot be used** — Bliss and Glow is your original project ✅
- Every group must have a **distinct project**.
- **Mandatory tech:** Java, Java EE, MySQL, JSP — failure to use these = automatic fail.
- Test on **two different PCs** before submitting.
- **Viva attendance is mandatory** — non-attendance = fail.
- **Plagiarism / contract cheating** is taken seriously — review your university regulations.

Good luck! 💚

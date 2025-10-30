# ♡ Heart of Bucharest 𐦂𖨆𐀪𖠋𐀪𐀪 

### Team Members  
- Bădică Ioana-Miruna, 1241EA
---

## Project Description  

**Heart of Bucharest** is a **web application** for a students' community-oriented **coffee shop in Bucharest** that is both a **learning and social hub for students**.  
 
As a customer of the coffee shop, you can:
- Discover and apply for **social or educational events**.  
- Earn **rewards** for participation and birthdays.  
- **Book** daily sandwiches, coffees, or snacks.  
- Subscribe to the **newsletter** for updates and offers.  

As an administrator of the coffee shop can:
- **Add and manage events.**  
- **Edit the website content** — banners, menus, and announcements.  
- **Monitor user activity** (accounts, event applications, food bookings).  
- Manage **loyalty bonuses and rewards** for frequent participants.  

---

## System Overview  

The system will be developed as a **web-based platform** using:  

- **Frontend:** HTML, CSS, JavaScript (Thymeleaf)  
- **Backend:** Java + Spring Boot (REST APIs)  
- **Database:** MySQL  

---

## Core Functionalities  

### For Customers  
- **Account Management**  
  - Register and log in securely.  
  - Subscribe to the **newsletter** for updates.  

- **Event Participation**  
  - See upcoming events and apply to attend.  
  - Receive automatic reminders and notifications.  

- **Loyalty & Rewards**  
  - Earn free products for participating to activities and being loyal to the community (e.g., after attending 3, 5, 10 events).  
  - Receive personalized **birthday bonuses**.  

- **Food & Drink Bookings**  
  - Pre-book favorite products (e.g., 2 sandwiches every day).  
  - Manage recurring or one-time bookings easily.  

---

### For Administrators  
- **Event Management**  
  - Create, edit, and delete events.  
  - View who applied for each event.  

- **Content Management**  
  - Edit homepage text, images, menus, and special offers.  

- **Customer Insights**  
  - View total registered users.  
  - Track food bookings and event participation.  
  - Manage rewards and loyalty progress.  

---

## Design Patterns Used  

### 1. **[Singleton](https://refactoring.guru/design-patterns/singleton) – Service Registry & Configuration Manager**  
**Used for:** managing shared services such as database connections, newsletter engine, and reward configuration. Using Singleton I can avoid multiple inconsistent service instances could cause performance issues and errors. 

**Why:**  
- Guarantees a single consistent instance of global services.  
- Prevents redundant connections and synchronization issues.  
- Centralizes configurations like reward thresholds and newsletter frequency.

---

### 2. **[Observer](https://refactoring.guru/design-patterns/observer) – Event Updates & Newsletter Notifications**  
**Used for:** sending updates when events are created or modified; broadcasting newsletters to all subscribers. Using this the system would **not** rely on inefficient polling or manual refresh.  

**Why:**  
- Enables **loose coupling** between event publishers and subscribers.  
- Supports automatic notifications via email or app.  
- Scales easily with new notification channels.
  
---

### 3. **[Builder](https://refactoring.guru/design-patterns/builder) – Event and Booking Creation**  
**Used for:** creating complex event or booking objects with multiple optional parameters. Using Builder I will avoid unclear code and avoid too many constructors.

**Why:**  
- Builds flexible and readable objects step by step.  
- Avoids massive constructors or repetitive setter chains. 

---

### 4. **[Strategy](https://refactoring.guru/design-patterns/strategy) – Ranking and Reward Algorithms**  
**Used for:** Sorting events (by date, popularity, or type) and determining how rewards are granted (e.g., by participation count or purchase frequency). This is the easiest way to add new algorithms without changing all the conditional logic of the base code.  

**Why:**  
- Encapsulates interchangeable ranking and reward algorithms.  
- Allows dynamic behavior changes without modifying base logic.  
- Makes adding new reward strategies simple and maintainable.

**Diagrams:**

![Class Diagram.png](src/main/resources/assets/Class%20Diagram.png)

![Sequence Diagram 1.png](src/main/resources/assets/Sequence%20Diagram%201.png)

![Sequence Diagram 2.png](src/main/resources/assets/Sequence%20Diagram%202.png)
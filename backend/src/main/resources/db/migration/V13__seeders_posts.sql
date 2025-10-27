-- Seed text-only posts for testing
-- Posts from various users with diverse content
-- Admin posts (id: 1)
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        1,
        'Welcome to 01Blog!',
        '# Welcome Everyone! 🎉

We''re excited to have you here on 01Blog. This is a space for students and developers to share their learning journey, discoveries, and progress.

Feel free to share your experiences, ask questions, and connect with fellow learners!',
        CURRENT_TIMESTAMP - INTERVAL '180 days',
        CURRENT_TIMESTAMP - INTERVAL '180 days'
    ),
    (
        1,
        'Community Guidelines Update',
        'Quick reminder about our community guidelines:

- Be respectful and supportive
- Share knowledge freely
- Report inappropriate content
- Help newcomers feel welcome

Let''s keep 01Blog a positive space for everyone! 💙',
        CURRENT_TIMESTAMP - INTERVAL '90 days',
        CURRENT_TIMESTAMP - INTERVAL '90 days'
    );

-- Sarah posts (id: 2) - Full-stack developer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        2,
        'Just deployed my first Spring Boot app!',
        'After weeks of learning, I finally deployed my first production Spring Boot application! 🚀

Key takeaways:
- Docker makes deployment so much easier
- Don''t forget to configure CORS properly
- Environment variables are your friend

Anyone else remember their first deployment? What challenges did you face?',
        CURRENT_TIMESTAMP - INTERVAL '145 days',
        CURRENT_TIMESTAMP - INTERVAL '145 days'
    ),
    (
        2,
        'Understanding Spring Security',
        '# Spring Security Deep Dive

Spent the weekend diving into Spring Security. Here''s what I learned:

**Key Concepts:**
- Authentication vs Authorization
- SecurityFilterChain
- JWT tokens for stateless auth

The documentation is dense but worth it. Taking notes really helps!',
        CURRENT_TIMESTAMP - INTERVAL '100 days',
        CURRENT_TIMESTAMP - INTERVAL '100 days'
    ),
    (
        2,
        'Coffee and Code ☕',
        'Perfect morning: freshly brewed coffee, my favorite IDE, and a challenging bug to solve.

Anyone else do their best debugging with coffee? What''s your go-to productivity drink?',
        CURRENT_TIMESTAMP - INTERVAL '15 days',
        CURRENT_TIMESTAMP - INTERVAL '15 days'
    );

-- Mike posts (id: 3) - Backend developer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        3,
        'Learning REST API Best Practices',
        '## REST API Design Tips

After building several APIs, here are my top tips:

1. Use proper HTTP methods (GET, POST, PUT, DELETE)
2. Version your API (/api/v1/)
3. Return meaningful status codes
4. Keep responses consistent
5. Document everything!

What practices do you follow?',
        CURRENT_TIMESTAMP - INTERVAL '135 days',
        CURRENT_TIMESTAMP - INTERVAL '135 days'
    ),
    (
        3,
        'Database Optimization Journey',
        'Optimized a slow query today and reduced execution time from 5 seconds to 50ms! 

The solution? Proper indexing on foreign keys. Such a simple fix with massive impact.

Never underestimate the power of database indexes! 📊',
        CURRENT_TIMESTAMP - INTERVAL '80 days',
        CURRENT_TIMESTAMP - INTERVAL '80 days'
    ),
    (
        3,
        'Started a new side project',
        'Working on a task management app to practice microservices architecture.

Tech stack:
- Spring Boot
- PostgreSQL
- Docker
- Redis for caching

Excited to see where this goes!',
        CURRENT_TIMESTAMP - INTERVAL '20 days',
        CURRENT_TIMESTAMP - INTERVAL '20 days'
    );

-- Emma posts (id: 4) - Frontend developer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        4,
        'Angular Reactive Forms are Amazing',
        'Just discovered the power of Angular Reactive Forms and I''m blown away! 🤯

FormBuilder and Validators make form handling so elegant. No more messy template-driven forms for complex use cases.

If you''re still using template-driven forms for everything, give Reactive Forms a try!',
        CURRENT_TIMESTAMP - INTERVAL '125 days',
        CURRENT_TIMESTAMP - INTERVAL '125 days'
    ),
    (
        4,
        'CSS Grid vs Flexbox',
        '# When to Use What?

**CSS Grid:** 2D layouts, page-level structure
**Flexbox:** 1D layouts, component-level alignment

Both are powerful, but knowing when to use each makes your life easier.

My rule of thumb: Start with Flexbox, switch to Grid when you need rows AND columns.',
        CURRENT_TIMESTAMP - INTERVAL '70 days',
        CURRENT_TIMESTAMP - INTERVAL '70 days'
    ),
    (
        4,
        'Finally understanding RxJS',
        'RxJS clicked for me today! It''s all about streams of data.

Think of it like water flowing through pipes - you can filter it, transform it, combine it.

Still have a lot to learn but the "aha moment" feels great! 💡',
        CURRENT_TIMESTAMP - INTERVAL '12 days',
        CURRENT_TIMESTAMP - INTERVAL '12 days'
    );

-- James posts (id: 5) - DevOps engineer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        5,
        'Docker Compose for Local Development',
        'Set up my entire dev environment with Docker Compose today. Game changer! 🐳

```yaml
version: ''3.8''
services:
  db:
    image: postgres:15
  backend:
    build: ./backend
  frontend:
    build: ./frontend
```

One command to rule them all: `docker-compose up`',
        CURRENT_TIMESTAMP - INTERVAL '115 days',
        CURRENT_TIMESTAMP - INTERVAL '115 days'
    ),
    (
        5,
        'Kubernetes is Hard But Worth It',
        'Been learning Kubernetes for the past month. Steep learning curve but the benefits are clear:

- Automatic scaling
- Self-healing
- Rolling updates
- Service discovery

Start with minikube for local practice!',
        CURRENT_TIMESTAMP - INTERVAL '55 days',
        CURRENT_TIMESTAMP - INTERVAL '55 days'
    );

-- Olivia posts (id: 6) - Data scientist
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        6,
        'Python for Data Analysis',
        '# Pandas is Powerful 🐍

Working on a data analysis project and pandas continues to amaze me.

DataFrame operations are so intuitive once you get the hang of it.

Favorite methods:
- groupby()
- merge()
- pivot_table()

What''s yours?',
        CURRENT_TIMESTAMP - INTERVAL '105 days',
        CURRENT_TIMESTAMP - INTERVAL '105 days'
    ),
    (
        6,
        'Machine Learning Model Deployed!',
        'Deployed my first ML model to production today! 

Used Flask to create a simple API endpoint. Model predicts customer churn with 87% accuracy.

Exciting to see theory become practice! 🎯',
        CURRENT_TIMESTAMP - INTERVAL '45 days',
        CURRENT_TIMESTAMP - INTERVAL '45 days'
    );

-- William posts (id: 7) - Mobile developer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        7,
        'React Native Tips',
        'Building my first React Native app. Quick tips for beginners:

1. Use Expo for rapid prototyping
2. Test on real devices early
3. Platform-specific code is okay
4. Performance matters on mobile

Anyone else in mobile dev?',
        CURRENT_TIMESTAMP - INTERVAL '95 days',
        CURRENT_TIMESTAMP - INTERVAL '95 days'
    ),
    (
        7,
        'App Store Submission',
        'Just submitted my app to the App Store! 

The review process is nerve-wracking. Fingers crossed it gets approved on the first try 🤞

How long did your first submission take?',
        CURRENT_TIMESTAMP - INTERVAL '30 days',
        CURRENT_TIMESTAMP - INTERVAL '30 days'
    );

-- Sophia posts (id: 8) - Software engineer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        8,
        'Code Review Best Practices',
        '## Things I Look For in Code Reviews

✅ Clear naming conventions
✅ Proper error handling
✅ Unit tests included
✅ No duplicated code
✅ Comments where necessary

Code reviews make us all better developers!',
        CURRENT_TIMESTAMP - INTERVAL '92 days',
        CURRENT_TIMESTAMP - INTERVAL '92 days'
    ),
    (
        8,
        'Contributing to Open Source',
        'Made my first open source contribution today! 🎉

It was just fixing a typo in documentation, but you have to start somewhere.

Don''t be intimidated - maintainers are usually very welcoming to new contributors.',
        CURRENT_TIMESTAMP - INTERVAL '25 days',
        CURRENT_TIMESTAMP - INTERVAL '25 days'
    ),
    (
        8,
        'Debugging Tips',
        'My debugging workflow:

1. Read the error message carefully
2. Check recent changes
3. Use console.log / debugger strategically
4. Google the error
5. Take a break if stuck

The break is often the most important step!',
        CURRENT_TIMESTAMP - INTERVAL '5 days',
        CURRENT_TIMESTAMP - INTERVAL '5 days'
    );

-- Benjamin posts (id: 9) - Security researcher
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        9,
        'Web Security Basics',
        '# Security 101 for Web Developers

Essential practices:
- Always validate user input
- Use HTTPS everywhere
- Implement rate limiting
- Hash passwords properly (bcrypt!)
- Keep dependencies updated

Security is everyone''s responsibility! 🔒',
        CURRENT_TIMESTAMP - INTERVAL '88 days',
        CURRENT_TIMESTAMP - INTERVAL '88 days'
    ),
    (
        9,
        'XSS Prevention',
        'Cross-Site Scripting (XSS) attacks are still common in 2024.

Simple prevention:
- Sanitize user input
- Use Content Security Policy headers
- Escape output
- Use frameworks that auto-escape

Don''t trust user input. Ever.',
        CURRENT_TIMESTAMP - INTERVAL '40 days',
        CURRENT_TIMESTAMP - INTERVAL '40 days'
    );

-- Isabella posts (id: 10) - Product designer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        10,
        'UX Design Principles',
        'Good UX is invisible. Users don''t notice good design - they just enjoy using the product.

Key principles:
- Consistency
- Clear feedback
- Error prevention
- User control

What''s your favorite app in terms of UX?',
        CURRENT_TIMESTAMP - INTERVAL '82 days',
        CURRENT_TIMESTAMP - INTERVAL '82 days'
    ),
    (
        10,
        'Figma is a Game Changer',
        'Switched to Figma for all my design work. The collaboration features are incredible!

Real-time editing with team members feels like magic ✨

Any other Figma users here?',
        CURRENT_TIMESTAMP - INTERVAL '35 days',
        CURRENT_TIMESTAMP - INTERVAL '35 days'
    );

-- Lucas posts (id: 11) - Backend architect
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        11,
        'Microservices Architecture',
        '## Moving from Monolith to Microservices

Key lessons learned:
- Start with a monolith
- Split when you have clear boundaries
- API gateway is essential
- Service discovery matters
- Monitor everything

Microservices aren''t always the answer!',
        CURRENT_TIMESTAMP - INTERVAL '78 days',
        CURRENT_TIMESTAMP - INTERVAL '78 days'
    ),
    (
        11,
        'Event-Driven Architecture',
        'Implementing event-driven architecture with message queues (RabbitMQ).

Benefits:
- Loose coupling
- Better scalability
- Async processing
- Easier to add new features

The learning curve is steep but worth it!',
        CURRENT_TIMESTAMP - INTERVAL '18 days',
        CURRENT_TIMESTAMP - INTERVAL '18 days'
    );

-- Mia posts (id: 12) - Junior developer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        12,
        'My First Month as a Developer',
        'Completed my first month as a junior developer! 🎉

Things I learned:
- Asking questions is good
- Everyone uses Google
- Imposter syndrome is real
- Code reviews help you learn
- It''s okay not to know everything

To other juniors: we got this! 💪',
        CURRENT_TIMESTAMP - INTERVAL '73 days',
        CURRENT_TIMESTAMP - INTERVAL '73 days'
    ),
    (
        12,
        'JavaScript Quirks',
        'JavaScript is weird but fun:

`[] + [] = ''''`
`[] + {} = [object Object]`
`{} + [] = 0`

Understanding these quirks helps you write better code!

What''s the weirdest JS behavior you''ve seen?',
        CURRENT_TIMESTAMP - INTERVAL '22 days',
        CURRENT_TIMESTAMP - INTERVAL '22 days'
    ),
    (
        12,
        'Learning Resources',
        'My favorite free learning resources:

- MDN Web Docs
- freeCodeCamp
- JavaScript.info
- CSS-Tricks
- Dev.to

What are yours?',
        CURRENT_TIMESTAMP - INTERVAL '8 days',
        CURRENT_TIMESTAMP - INTERVAL '8 days'
    );

-- Henry posts (id: 13) - Game developer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        13,
        'Unity Game Development',
        'Started learning Unity game development. The engine is powerful but overwhelming at first.

Tips for beginners:
- Start with 2D games
- Follow tutorials
- Understand the game loop
- Learn C# basics first

What was your first game project?',
        CURRENT_TIMESTAMP - INTERVAL '68 days',
        CURRENT_TIMESTAMP - INTERVAL '68 days'
    ),
    (
        13,
        'Game Jam Experience',
        'Participated in my first game jam! Made a simple platformer in 48 hours.

It''s rough but it''s DONE. Shipping > Perfection.

Game jams are great for learning and networking! 🎮',
        CURRENT_TIMESTAMP - INTERVAL '16 days',
        CURRENT_TIMESTAMP - INTERVAL '16 days'
    );

-- Charlotte posts (id: 14) - Tech writer
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        14,
        'Writing Technical Documentation',
        'Good documentation can make or break a project.

My checklist:
✅ Clear examples
✅ Installation steps
✅ Common errors section
✅ API reference
✅ Keep it updated

Developers are users too!',
        CURRENT_TIMESTAMP - INTERVAL '63 days',
        CURRENT_TIMESTAMP - INTERVAL '63 days'
    ),
    (
        14,
        'Markdown for Everything',
        'Markdown is perfect for technical writing:

- Easy to write
- Easy to read
- Version control friendly
- Converts to anything

README, docs, notes - Markdown does it all! ✍️',
        CURRENT_TIMESTAMP - INTERVAL '28 days',
        CURRENT_TIMESTAMP - INTERVAL '28 days'
    );

-- Alexander posts (id: 15) - AI researcher
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        15,
        'Deep Learning Journey',
        'Started a deep learning course. Neural networks are fascinating!

Currently learning:
- Backpropagation
- Convolutional Neural Networks
- Transfer learning

The math is challenging but rewarding.',
        CURRENT_TIMESTAMP - INTERVAL '58 days',
        CURRENT_TIMESTAMP - INTERVAL '58 days'
    ),
    (
        15,
        'AI Ethics Matter',
        'As AI becomes more powerful, ethical considerations become more important.

We need to think about:
- Bias in training data
- Privacy concerns
- Transparency
- Accountability

Build responsibly! 🤖',
        CURRENT_TIMESTAMP - INTERVAL '14 days',
        CURRENT_TIMESTAMP - INTERVAL '14 days'
    );

-- Recent posts from other users
INSERT INTO
    posts (user_id, title, content, created_at, updated_at)
VALUES
    (
        16,
        'Test Automation Tips',
        'Automated 200+ test cases this week. Key takeaway: good tests save time but bad tests waste it.

Write tests that are:
- Fast
- Reliable
- Maintainable
- Focused

Quality over quantity!',
        CURRENT_TIMESTAMP - INTERVAL '10 days',
        CURRENT_TIMESTAMP - INTERVAL '10 days'
    ),
    (
        17,
        'Blockchain Basics',
        'Learning blockchain development. It''s not just about crypto!

Real use cases:
- Supply chain tracking
- Digital identity
- Smart contracts
- Decentralized apps

The technology is revolutionary.',
        CURRENT_TIMESTAMP - INTERVAL '19 days',
        CURRENT_TIMESTAMP - INTERVAL '19 days'
    ),
    (
        20,
        'SQL Query Optimization',
        'Optimized a complex query today:

Before: 30 seconds ⏱️
After: 0.3 seconds ⚡

Changes:
- Added proper indexes
- Removed unnecessary joins
- Used EXPLAIN to understand execution plan

Database performance is an art!',
        CURRENT_TIMESTAMP - INTERVAL '11 days',
        CURRENT_TIMESTAMP - INTERVAL '11 days'
    ),
    (
        24,
        'Teaching Coding on YouTube',
        'Published my first coding tutorial on YouTube! 

Topic: JavaScript array methods

It''s scary putting yourself out there but the feedback has been positive.

Fellow content creators, any tips?',
        CURRENT_TIMESTAMP - INTERVAL '9 days',
        CURRENT_TIMESTAMP - INTERVAL '9 days'
    ),
    (
        26,
        'Algorithm Practice',
        'Solving LeetCode problems daily to prepare for interviews.

Current streak: 30 days 🔥

Favorite problem types:
- Two pointers
- Sliding window
- Dynamic programming

Anyone else grinding algorithms?',
        CURRENT_TIMESTAMP - INTERVAL '6 days',
        CURRENT_TIMESTAMP - INTERVAL '6 days'
    ),
    (
        28,
        'Bootcamp Graduate',
        'Just graduated from coding bootcamp! 

The journey was intense but worth it. From zero coding knowledge to building full-stack apps in 3 months.

Now the job hunt begins. Wish me luck! 🍀',
        CURRENT_TIMESTAMP - INTERVAL '4 days',
        CURRENT_TIMESTAMP - INTERVAL '4 days'
    ),
    (
        30,
        'Working Remotely from Bali',
        'Living the digital nomad dream in Bali 🏝️

Morning: Beach and coffee
Day: Coding
Evening: Sunset

Remote work is amazing but time zones can be tricky!',
        CURRENT_TIMESTAMP - INTERVAL '2 days',
        CURRENT_TIMESTAMP - INTERVAL '2 days'
    );
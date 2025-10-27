INSERT INTO
    users (
        first_name,
        last_name,
        username,
        email,
        password,
        role,
        bio,
        created_at
    )
VALUES
    -- User 1: Admin
    (
        'yassine',
        'el mach',
        'yelmach',
        'yassine@01blog.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'ADMIN',
        'Platform administrator. Keeping 01Blog safe and awesome!',
        CURRENT_TIMESTAMP - INTERVAL '180 days'
    ),
    -- User 2
    (
        'Sarah',
        'Johnson',
        'sarah_j',
        'sarah.johnson@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Full-stack developer | Coffee enthusiast ☕ | Learning Spring Boot',
        CURRENT_TIMESTAMP - INTERVAL '150 days'
    ),
    -- User 3
    (
        'Michael',
        'Chen',
        'mike_chen',
        'michael.chen@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'CS student | Backend developer | Tech blogger | Sharing my learning journey 🚀',
        CURRENT_TIMESTAMP - INTERVAL '140 days'
    ),
    -- User 4
    (
        'Emma',
        'Davis',
        'emma_codes',
        'emma.davis@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Frontend wizard ✨ | Angular enthusiast | UI/UX lover',
        CURRENT_TIMESTAMP - INTERVAL '130 days'
    ),
    -- User 5
    (
        'James',
        'Wilson',
        'james_w',
        'james.wilson@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'DevOps engineer | Docker & Kubernetes | Automation is life',
        CURRENT_TIMESTAMP - INTERVAL '120 days'
    ),
    -- User 6
    (
        'Olivia',
        'Martinez',
        'olivia_m',
        'olivia.martinez@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Data scientist | Python lover 🐍 | ML enthusiast',
        CURRENT_TIMESTAMP - INTERVAL '110 days'
    ),
    -- User 7
    (
        'William',
        'Anderson',
        'will_a',
        'william.anderson@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Mobile developer | React Native | Building apps that matter',
        CURRENT_TIMESTAMP - INTERVAL '100 days'
    ),
    -- User 8
    (
        'Sophia',
        'Taylor',
        'sophia_dev',
        'sophia.taylor@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Software engineer | Code reviewer | Open source contributor 💻',
        CURRENT_TIMESTAMP - INTERVAL '95 days'
    ),
    -- User 9
    (
        'Benjamin',
        'Thomas',
        'ben_t',
        'benjamin.thomas@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Security researcher | Ethical hacker | Cybersecurity student',
        CURRENT_TIMESTAMP - INTERVAL '90 days'
    ),
    -- User 10
    (
        'Isabella',
        'Garcia',
        'bella_g',
        'isabella.garcia@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Product designer | UX research | Making tech more human 🎨',
        CURRENT_TIMESTAMP - INTERVAL '85 days'
    ),
    -- User 11
    (
        'Lucas',
        'Rodriguez',
        'lucas_r',
        'lucas.rodriguez@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Backend architect | Microservices | Cloud computing enthusiast',
        CURRENT_TIMESTAMP - INTERVAL '80 days'
    ),
    -- User 12
    (
        'Mia',
        'Hernandez',
        'mia_codes',
        'mia.hernandez@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Junior developer | Learning every day | JavaScript ninja 🥷',
        CURRENT_TIMESTAMP - INTERVAL '75 days'
    ),
    -- User 13
    (
        'Henry',
        'Lopez',
        'henry_l',
        'henry.lopez@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Game developer | Unity & Unreal | Creating immersive experiences 🎮',
        CURRENT_TIMESTAMP - INTERVAL '70 days'
    ),
    -- User 14
    (
        'Charlotte',
        'Gonzalez',
        'char_g',
        'charlotte.gonzalez@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Tech writer | Documentation lover | Making complex things simple ✍️',
        CURRENT_TIMESTAMP - INTERVAL '65 days'
    ),
    -- User 15
    (
        'Alexander',
        'Wilson',
        'alex_w',
        'alexander.wilson@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'AI researcher | Deep learning | Computer vision enthusiast',
        CURRENT_TIMESTAMP - INTERVAL '60 days'
    ),
    -- User 16
    (
        'Amelia',
        'Martinez',
        'amelia_m',
        'amelia.martinez@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'QA engineer | Test automation | Breaking things so you don''t have to 🔍',
        CURRENT_TIMESTAMP - INTERVAL '55 days'
    ),
    -- User 17
    (
        'Daniel',
        'Lee',
        'dan_lee',
        'daniel.lee@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Blockchain developer | Crypto enthusiast | Web3 builder',
        CURRENT_TIMESTAMP - INTERVAL '50 days'
    ),
    -- User 18
    (
        'Evelyn',
        'White',
        'eve_w',
        'evelyn.white@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'System admin | Linux guru | Shell scripting master 🐧',
        CURRENT_TIMESTAMP - INTERVAL '45 days'
    ),
    -- User 19
    (
        'Matthew',
        'Harris',
        'matt_h',
        'matthew.harris@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Startup founder | Tech entrepreneur | Building the future 🚀',
        CURRENT_TIMESTAMP - INTERVAL '40 days'
    ),
    -- User 20
    (
        'Harper',
        'Clark',
        'harper_c',
        'harper.clark@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Database administrator | SQL expert | Data modeling pro',
        CURRENT_TIMESTAMP - INTERVAL '35 days'
    ),
    -- User 21
    (
        'Ethan',
        'Lewis',
        'ethan_l',
        'ethan.lewis@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Cloud architect | AWS certified | Infrastructure as code advocate',
        CURRENT_TIMESTAMP - INTERVAL '30 days'
    ),
    -- User 22
    (
        'Abigail',
        'Walker',
        'abby_w',
        'abigail.walker@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Scrum master | Agile coach | Team enabler 🎯',
        CURRENT_TIMESTAMP - INTERVAL '28 days'
    ),
    -- User 23
    (
        'Sebastian',
        'Hall',
        'seb_h',
        'sebastian.hall@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'IoT developer | Arduino & Raspberry Pi | Hardware meets software 🔌',
        CURRENT_TIMESTAMP - INTERVAL '25 days'
    ),
    -- User 24
    (
        'Emily',
        'Allen',
        'emily_a',
        'emily.allen@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Tech content creator | YouTube | Teaching coding to beginners 📹',
        CURRENT_TIMESTAMP - INTERVAL '22 days'
    ),
    -- User 25
    (
        'Jackson',
        'Young',
        'jack_y',
        'jackson.young@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'API developer | RESTful services | GraphQL explorer',
        CURRENT_TIMESTAMP - INTERVAL '20 days'
    ),
    -- User 26
    (
        'Avery',
        'King',
        'avery_k',
        'avery.king@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Computer science student | Algorithm enthusiast | Competitive programmer 💡',
        CURRENT_TIMESTAMP - INTERVAL '18 days'
    ),
    -- User 27
    (
        'David',
        'Wright',
        'dave_w',
        'david.wright@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Site reliability engineer | Monitoring & observability | 99.99% uptime crusader',
        CURRENT_TIMESTAMP - INTERVAL '15 days'
    ),
    -- User 28
    (
        'Ella',
        'Lopez',
        'ella_l',
        'ella.lopez@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Tech bootcamp graduate | Career changer | Loving my new coding journey 🌟',
        CURRENT_TIMESTAMP - INTERVAL '12 days'
    ),
    -- User 29
    (
        'Joseph',
        'Hill',
        'joe_h',
        'joseph.hill@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Network engineer | Cisco certified | Connecting the world 🌐',
        CURRENT_TIMESTAMP - INTERVAL '10 days'
    ),
    -- User 30
    (
        'Sofia',
        'Scott',
        'sofia_s',
        'sofia.scott@email.com',
        '$2a$10$ylGHjWFEiMfXnZGuG8pjx.lpp9Hh.lKeCbKSJ.xExCLU1sHhCz2uS',
        'USER',
        'Digital nomad | Remote developer | Coding from beaches 🏖️',
        CURRENT_TIMESTAMP - INTERVAL '7 days'
    );
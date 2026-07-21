INSERT INTO plans (id, name, slug, price_monthly, price_yearly, max_units, max_staff, is_active)
VALUES
    (gen_random_uuid(), 'Trial',      'trial',      0,      0,       50,  2,  TRUE),
    (gen_random_uuid(), 'Starter',    'starter',    999,    9990,    100, 5,  TRUE),
    (gen_random_uuid(), 'Growth',     'growth',     2499,   24990,   500, 20, TRUE),
    (gen_random_uuid(), 'Enterprise', 'enterprise', 0,      0,       9999, 100, TRUE);

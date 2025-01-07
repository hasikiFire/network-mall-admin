INSERT INTO
  `user_coupon` (
    `code`,
    `content`,
    `limit`,
    `use_count`,
    `create_time`,
    `expire_time`
  )
VALUES
  (
    'FREE100',
    '{"discount_type": "percentage", "amount": 100}',
    '{}',
    0,
    NOW(),
    '2023-12-31 23:59:59'
  );
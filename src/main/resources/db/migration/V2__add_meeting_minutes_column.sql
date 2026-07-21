-- Add minutes file path column to meetings (not in V1)
ALTER TABLE meetings ADD COLUMN IF NOT EXISTS minutes_file_path VARCHAR(1000);

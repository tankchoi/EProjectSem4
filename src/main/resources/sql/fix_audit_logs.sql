-- Fix audit_logs table columns to support longer JSON data
ALTER TABLE audit_logs MODIFY COLUMN old_value TEXT;
ALTER TABLE audit_logs MODIFY COLUMN new_value TEXT;

import "./AdminTable.css";

const AdminTable = ({ columns, data, actions, loading = false }) => {
  return (
    <div className="admin-table-wrapper">
      <table className="admin-table">
        <thead>
          <tr>
            {columns.map((c) => (
              <th key={c}>{c.toUpperCase()}</th>
            ))}
            <th className="actions-col">Actions</th>
          </tr>
        </thead>

        <tbody>
          {loading ? (
            <tr>
              <td colSpan={columns.length + 1} className="loading-cell">
                Loading...
              </td>
            </tr>
          ) : data.length === 0 ? (
            <tr>
              <td colSpan={columns.length + 1} className="empty-cell">
                No records found
              </td>
            </tr>
          ) : (
            data.map((row) => (
              <tr key={row.id} className="table-row">
                {columns.map((c) => (
                  <td key={c} className="cell">
                    {row[c]}
                  </td>
                ))}
                <td className="actions-cell">{actions(row)}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default AdminTable;

import "./AdminTable.css";

const AdminTable = ({ columns, data, actions, loading = false }) => {
  return (
    <div className="admin-table-wrapper">
      <table className="admin-table">
        <thead>
          <tr>
            {columns.map((c, i) => {
              const headerText = typeof c === "string" ? c : c.header || "";
              return <th key={i}>{headerText.toUpperCase()}</th>;
            })}
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
            data.map((row, rowIndex) => (
              <tr key={row.id || rowIndex} className="table-row">
                {columns.map((c, colIndex) => {
                  const key = typeof c === "string" ? c : c.key;
                  const value = row[key];
                  return (
                    <td key={colIndex} className="cell">
                      {typeof c === "object" && c.render
                        ? c.render(value, row)
                        : value}
                    </td>
                  );
                })}
                <td className="actions-cell">
                  {actions ? actions(row) : "-"}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default AdminTable;
